package com.bookshop.bookshop.service.implementation;

import com.bookshop.bookshop.exception.BadRequestException;
import com.bookshop.bookshop.exception.PremiumContentException;
import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.Love;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.PagedResponse;
import com.bookshop.bookshop.payload.StoryRequest;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.TopicRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.StoryService;
import com.bookshop.bookshop.util.ModelMapper;
import com.bookshop.bookshop.util.ValidatePageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.plaf.ColorUIResource;
import javax.xml.ws.Response;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StoryServiceImpl implements StoryService {


    public StoryServiceImpl(StoryRepository storyRepository, UserRepository userRepository, LoveRepository loveRepository,
                            TopicRepository topicRepository) {

        this.loveRepository = loveRepository;
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
        this.topicRepository = topicRepository;

    }


    final private StoryRepository storyRepository;
    final private UserRepository userRepository;
    final private LoveRepository loveRepository;
    final private TopicRepository topicRepository;

    private static final Logger logger = LoggerFactory.getLogger(StoryServiceImpl.class);



    public PagedResponse<StoryResponse> getAllStories(UserPrincipal currentUser, int page, int size) {


        ValidatePageUtil.validatePageNumberAndSize(page, size);


        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Story> stories = storyRepository.findAll(pageable);

        if(stories.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());
        }


        Map<Long, User> creatorMap = getCreatorsIdsAndCreatorOfStories(stories.getContent());


        List<StoryResponse> storyResponses = stories.map(story -> {
            long userLoveCount = loveRepository.countByStoryId(story.getId());

            return ModelMapper.mapStoryToStoryResponse(story, creatorMap.get(story.getCreatedBy())
                    , userLoveCount);
        }).getContent();

        return new PagedResponse<>(storyResponses, stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());

    }

    public PagedResponse<StoryResponse> getStoriesByTitle(String title, UserPrincipal userPrincipal, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Story> stories = storyRepository.findByTitle(title, pageable);

        if(stories.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());
        }

        Map<Long, User> creatorMap = getCreatorsIdsAndCreatorOfStories(stories.getContent());

        List<StoryResponse> storyResponses = stories.map(story -> {
            long userLoveCount = loveRepository.countByStoryId(story.getId());

            return ModelMapper.mapStoryToStoryResponse(story, creatorMap.get(story.getCreatedBy())
                    , userLoveCount);
        }).getContent();

        return new PagedResponse<>(storyResponses, stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());
    }

    public PagedResponse<StoryResponse> getStoriesCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Story> stories = storyRepository.findByCreatedBy(user.getId(), pageable);

        if(stories.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), stories.getNumber(), stories.getSize(), stories.getTotalElements(),stories.getTotalPages(), stories.isLast());
        }

        //Map Stories to StoryResponse containing love count and story creator details
        List<Long> storyIds = stories.map(Story::getId).getContent();

        Map<Long, Long> storyUserLoveMap = getCurrentUserStoryIds(currentUser, storyIds);

        List<StoryResponse> storyResponses = stories.map(story -> {
            long userLoveCount = loveRepository.countByStoryId(story.getId());
            return ModelMapper.mapStoryToStoryResponse(story, user, userLoveCount);
        }).getContent();


        return new PagedResponse<>(storyResponses, stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());

    }

    public PagedResponse<StoryResponse> getStoriesLovedBy(String username, UserPrincipal currentUser, int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

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
        Map<Long, Long> storyUserLoveMap = getCurrentUserStoryIds(currentUser, storyIds);
        Map<Long, User> creatorMap = getCreatorsIdsAndCreatorOfStories(stories);

        List<StoryResponse> storyResposes = stories.stream().map(story -> {

            long userLoveCount = loveRepository.countByUserId(story.getId());

            return ModelMapper.mapStoryToStoryResponse(story
                    , creatorMap.get(story.getCreatedBy()), storyUserLoveMap == null ? null : storyUserLoveMap.getOrDefault(story.getId(), userLoveCount));
        }).collect(Collectors.toList());

        return new PagedResponse<>(storyResposes, userLovedStoryIds.getNumber(), userLovedStoryIds.getSize(), userLovedStoryIds.getTotalElements(), userLovedStoryIds.getTotalPages(), userLovedStoryIds.isLast());
    }

    public Story createStory(StoryRequest storyRequest, Long topicId) {

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        Story story = new Story();
        story.setTopic(topic);
        story.setPremiumContent(storyRequest.isPremiumContent());
        story.setBody(storyRequest.getBody());
        story.setDescription(storyRequest.getDescription());
        story.setTitle(storyRequest.getTitle());


        return storyRepository.save(story);

    }

    public StoryResponse getStoryById(Long storyId, UserPrincipal currentUser) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Poll", "id", storyId));

        //Retrieve story creator details
        User creator = userRepository.findById(story.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", story.getCreatedBy()));

        
        long totalLoves = loveRepository.countByStoryId(storyId);

        return ModelMapper.mapStoryToStoryResponse(story, creator, totalLoves);

    }

    public StoryResponse castLoveAndGetUpdateStory(Long storyId, UserPrincipal currentUser) {
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


        User creator = userRepository.findById(story.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", story.getCreatedBy()));

        long totalLoves = loveRepository.countByStoryId(storyId);

        return ModelMapper.mapStoryToStoryResponse(story, creator, totalLoves);

    }

    public PagedResponse<StoryResponse> getStoryByTopicId(Long topicId, UserPrincipal currentUser,  int page, int size) {
        ValidatePageUtil.validatePageNumberAndSize(page, size);

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));


        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Story> stories = storyRepository.findByTopicId(topic.getId(), pageable);

        if(stories.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), stories.getNumber(), stories.getSize(), stories.getTotalElements(),stories.getTotalPages(), stories.isLast());
        }

        List<Long> storyIds = stories.map(Story::getId).getContent();

        Map<Long, Long> storyUserLoveMap = getCurrentUserStoryIds(currentUser, storyIds);

        List<StoryResponse> storyResponses = stories.map(story -> {

            User user = userRepository.findById(story.getCreatedBy())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", story.getCreatedBy()));

            long userLoveCount = loveRepository.countByStoryId(story.getId());
            return ModelMapper.mapStoryToStoryResponse(story, user, userLoveCount);
        }).getContent();

        return new PagedResponse<>(storyResponses, stories.getNumber(), stories.getSize(), stories.getTotalElements(), stories.getTotalPages(), stories.isLast());

    }

    public StoryResponse deleteLoveAndGetUpdateStory(Long storyId, UserPrincipal currentUser) {
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "id", storyId));

        User creator = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "user", currentUser.getId()));

        Love love = loveRepository.findByUserIdAndStoryId(creator.getId(), story.getId());

        if(love == null) {
            throw new BadRequestException("Sorry, You are not cast this love");
        } else {
            return deleteAndReturnNewStory(story, love, creator);
        }

    }


    public ResponseEntity<?> deleteStory(Long storyId, UserPrincipal currentUser) {

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "user", currentUser.getId()));

        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "id", storyId));

        long creationId = user.getId();

        long storyCreatorId = story.getCreatedBy();

        if(creationId == storyCreatorId) {
            storyRepository.delete(story);
            return ResponseEntity.ok(new ApiResponse(true, "Story deleted successfully"));
        } else {
            throw new BadRequestException("Sorry you not created this story");
        }


    }




    public StoryResponse updateStory(StoryRequest storyRequest, Long storyId, UserPrincipal currentUser)  {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        Story story = storyRepository.findById(storyId).orElseThrow(() -> new ResourceNotFoundException("Story", "id", storyId));

        long creationId = user.getId();

        long storyCreatorId = story.getCreatedBy();

        if (creationId == storyCreatorId) {
            story.setTitle(storyRequest.getTitle());
            story.setBody(storyRequest.getBody());
            story.setDescription(storyRequest.getDescription());

            Story updatedStory = storyRepository.save(story);

            long storyLoveCount = loveRepository.countByStoryId(storyId);

            return ModelMapper.mapStoryToStoryResponse(updatedStory, user, storyLoveCount);
        } else {
            throw new BadRequestException("Sorry you not created this story");
        }
    }

    public Map<Long, User> getCreatorsIdsAndCreatorOfStories(List<Story> stories) {


        List<Long> creatorIds = stories.stream()
                .map(Story::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);

        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;

    }


    public Map<Long, Long> getCurrentUserStoryIds(UserPrincipal currentUser, List<Long> storyIds) {

        Map<Long, Long> storyUserLoveMap = null;

        if(currentUser != null) {

            List<Love> userLoves = loveRepository.findByUserIdAndStoryIdIn(currentUser.getId(), storyIds);

            storyUserLoveMap = userLoves.stream()
                    .collect(Collectors.toMap(love -> love.getStory().getId(), love -> love.getId()));
        }

        return storyUserLoveMap;
    }

    public StoryResponse deleteAndReturnNewStory(Story story, Love love, User creator) {
        loveRepository.delete(love);
        long totalLoves = loveRepository.countByStoryId(story.getId());
        return ModelMapper.mapStoryToStoryResponse(story, creator, totalLoves);
    }

    public void hasUserPremium(UserPrincipal currentUser, Story story) {

        boolean hasUserPremium = currentUser.isPremium();
        boolean hasStoryPremiumContent = currentUser.isPremium();

        boolean isUserHasPremiumAndHasStoryPremiumContent = hasUserPremium && hasStoryPremiumContent;
        boolean isHasUserPremiumAndHasStoryPremiumContent = !hasUserPremium && hasStoryPremiumContent;
        boolean isPremium = !isUserHasPremiumAndHasStoryPremiumContent || isHasUserPremiumAndHasStoryPremiumContent;

        
       if(isPremium) {
           throw new PremiumContentException("You have to have a premium account to see this story");
       }

    }



}
