package com.bookshop.bookshop.service;

import com.bookshop.bookshop.exception.BadRequestException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.Love;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.StoryLoveCount;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.LoveRequest;
import com.bookshop.bookshop.payload.PagedResponse;
import com.bookshop.bookshop.payload.StoryRequest;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.util.AppConstants;
import com.bookshop.bookshop.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoveRepository loveRepository;

    private static final Logger logger = LoggerFactory.getLogger(StoryService.class);

    public PagedResponse<StoryResponse> getAllStories(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Story> stories = storyRepository.findAll(pageable);

        if(stories.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());
        }

        List<Long> storiesIds = stories.map(Story::getId).getContent();
        Map<Long, Long> storyUserLoveMap = getStoryUserLoveMap(currentUser, storiesIds);
        Map<Long, User> creatorMap = getStoryCreatorMap(stories.getContent());

        List<StoryResponse> storyResponses = stories.map(story -> {
            long userLoveCount = loveRepository.countByStoryId(story.getId());
            System.out.println(userLoveCount);

            return ModelMapper.mapStoryToStoryResponse(story, creatorMap.get(story.getCreatedBy())
                    , userLoveCount);
        }).getContent();

        return new PagedResponse<>(storyResponses, stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());
    }

    public PagedResponse<StoryResponse> getStoriesCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Story> stories = storyRepository.findByCreatedBy(user.getId(), pageable);

        if(stories.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), stories.getNumber(), stories.getSize(), stories.getTotalElements(),stories.getTotalPages(), stories.isLast());
        }

        //Map Stories to StoryResponse containing love count and story creator details
        List<Long> storyIds = stories.map(Story::getId).getContent();

        Map<Long, Long> storyUserLoveMap = getStoryUserLoveMap(currentUser, storyIds);

        List<StoryResponse> storyResponses = stories.map(story -> {
            long userLoveCount = loveRepository.countByStoryId(story.getId());
            return ModelMapper.mapStoryToStoryResponse(story, user, userLoveCount);
        }).getContent();


        return new PagedResponse<>(storyResponses, stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());

    }

    public PagedResponse<StoryResponse> getStoriesLovedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        //Retrieve all storyIds in which the given username has loved
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Long> userLovedStoryIds = loveRepository.findLoveStoryIdsByUserId(user.getId(), pageable);

        if(userLovedStoryIds.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), userLovedStoryIds.getNumber(), userLovedStoryIds.getSize(), userLovedStoryIds.getTotalElements(), userLovedStoryIds.getTotalPages(), userLovedStoryIds.isLast());
        }

        //Retrieve all story details from the loved storyIds
        List<Long> storyIds = userLovedStoryIds.getContent();

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Story> stories = storyRepository.findByIdIn(storyIds, sort);

        //Map Stories to StoryResponses containing love counts and story creator details
        Map<Long, Long> storyUserLoveMap = getStoryUserLoveMap(currentUser, storyIds);
        Map<Long, User> creatorMap = getStoryCreatorMap(stories);

        List<StoryResponse> storyResposes = stories.stream().map(story -> {

            long userLoveCount = loveRepository.countByUserId(story.getId());

            return ModelMapper.mapStoryToStoryResponse(story
                    , creatorMap.get(story.getCreatedBy()), storyUserLoveMap == null ? null : storyUserLoveMap.getOrDefault(story.getId(), userLoveCount));
        }).collect(Collectors.toList());

        return new PagedResponse<>(storyResposes, userLovedStoryIds.getNumber(), userLovedStoryIds.getSize(), userLovedStoryIds.getTotalElements(), userLovedStoryIds.getTotalPages(), userLovedStoryIds.isLast());
    }

    public Story createStory(StoryRequest storyRequest) {
        Story story = new Story();
        story.setBody(storyRequest.getBody());
        story.setDescription(storyRequest.getDescription());
        story.setTitle(storyRequest.getTitle());


        return storyRepository.save(story);

    }

    public StoryResponse getStoryById(Long storyId, UserPrincipal currentUser) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll", "id", storyId));


        //Retrieve Love Counts of every story
        List<StoryLoveCount> loves = loveRepository.countByStoryIdGroupByStoryId(storyId);

        Map<Long, Long> storyLovesMap = loves.stream()
                .collect(Collectors.toMap(StoryLoveCount::getStoryId, StoryLoveCount::getLoveCount));

        //Retrieve story creator details
        User creator = userRepository.findById(story.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", story.getCreatedBy()));

        //Retrieve love done by logged in user
        Love userLove = null;
        if(currentUser != null) {
            userLove = loveRepository.findByUserIdAndStoryId(currentUser.getId(), storyId);
        }

        long totalLoves = loveRepository.countByStoryId(storyId);

        return ModelMapper.mapStoryToStoryResponse(story, creator, totalLoves);

    }

    public StoryResponse castLoveAndGetUpadateStory(Long storyId, UserPrincipal currentUser) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "id", storyId));

        User user = userRepository.getOne(currentUser.getId());

        Love love = new Love();

        love.setStory(story);
        love.setUser(user);

        try {
            love = loveRepository.save(love);
        } catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already loved in Story {}", currentUser.getId(), storyId);
            throw new BadRequestException("Sorry! You hae already cast your love in this story");
        }

        // - Love Saved, Return the updated Story Response now

        List<StoryLoveCount> loves = loveRepository.countByStoryIdGroupByStoryId(storyId);

        Map<Long, Long> storyLovesMap = loves.stream().collect(Collectors.toMap(StoryLoveCount::getStoryId, StoryLoveCount::getLoveCount));

        //Retrieve story creator details
        User creator = userRepository.findById(story.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", story.getCreatedBy()));

        long totalLoves = loveRepository.countByStoryId(storyId);

        return ModelMapper.mapStoryToStoryResponse(story, creator, totalLoves);

    }

    private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    //Retrieve Story Creator details of the given list of stories
    Map<Long, User> getStoryCreatorMap(List<Story> stories) {


        List<Long> creatorIds = stories.stream()
                .map(Story::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);

        Map<Long, User> creatorMap = creators.stream()
        .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;

    }

    //Retrieve current user's storyIds
   private Map<Long, Long> getStoryUserLoveMap(UserPrincipal currentUser, List<Long> storyIds) {

        Map<Long, Long> storyUserLoveMap = null;

        if(currentUser != null) {

            List<Love> userLoves = loveRepository.findByUserIdAndStoryIdIn(currentUser.getId(), storyIds);

            storyUserLoveMap = userLoves.stream()
                   .collect(Collectors.toMap(love -> love.getStory().getId(), love -> love.getId()));
        }

        return storyUserLoveMap;
   }

}
