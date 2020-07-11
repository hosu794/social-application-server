package com.bookshop.bookshop.service.implementation;

import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.DBFile;
import com.bookshop.bookshop.model.Love;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.repository.DBFileRepository;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.StoryService;
import com.bookshop.bookshop.service.UserService;
import com.bookshop.bookshop.util.ValidatePageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    public UserServiceImpl(UserRepository userRepository, StoryRepository storyRepository, LoveRepository loveRepository, StoryService storyService, DBFileStorageServiceImpl dbFileStorageService) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
        this.loveRepository = loveRepository;
        this.storyService = storyService;
        this.dbFileStorageService = dbFileStorageService;
    }


    UserRepository userRepository;
    StoryRepository storyRepository;
    LoveRepository loveRepository;
    StoryService storyService;
    DBFileStorageServiceImpl dbFileStorageService;


    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {


        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        DBFile foundFile = dbFileStorageService.getFileByFilename(user.getId().toString());

        boolean isAvatarExist = foundFile == null;

        if(isAvatarExist) {
            return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        } else {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/downloadFile/")
                    .path(String.valueOf(foundFile.getId()))
                    .toUriString();
            return new UserSummary(currentUser.getId(),currentUser.getUsername(), currentUser.getName(), fileDownloadUri);

        }

    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(UsernameRequest usernameRequest) {
        Boolean isAvailable = !userRepository.existsByUsername(usernameRequest.getUsername());

        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserIdentityAvailability checkEmailAvailability(EmailRequest emailRequest) {
        Boolean isAvailable = !userRepository.existsByEmail(emailRequest.getEmail());

        return new UserIdentityAvailability(isAvailable);
    }

    @Override
    public UserProfile getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));



        long storyCount = storyRepository.countByCreatedBy(user.getId());
        long loveCount = loveRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), storyCount, loveCount);

        return userProfile;
    }

    @Override
    public PagedResponse<StoryResponse> getStoriesCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        return storyService.getStoriesCreatedBy(username, currentUser, page, size);
    }

    @Override
    public PagedResponse<StoryResponse> getStoriesLovedBy(String username, UserPrincipal currentUser, int page, int size) {
        return storyService.getStoriesLovedBy(username, currentUser, page, size);
    }

    @Override
    public LoveAvailability checkIsUserLovedStory(Long storyId, Long userId) {

        Boolean isAvailable = !loveRepository.existsByStoryIdAndUserId(storyId, userId);

        return new LoveAvailability(isAvailable);
    }

    @Override
    public UserStoryCount getUserStoriesStatistics(long userId, UserPrincipal currentUser, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Long> userLovedStoryIds = loveRepository.findLoveStoryIdsByUserId(user.getId(), pageable);

        List<Long> storyIds = userLovedStoryIds.getContent();

        long userStoriesCount = storyRepository.countByCreatedBy(user.getId());

        long totalLovedByUser = loveRepository.countByUserId(user.getId());

        Page<Story> storiesPage = storyRepository.findByCreatedBy(user.getId(), pageable);

        List<Long> stories = storiesPage.map(Story::getId).getContent();

        List<Love> loves = loveRepository.findByStoryIdIn(stories);

        long totalLovesOnUserStories = loves.stream().count();

       return new UserStoryCount(userStoriesCount, totalLovedByUser, totalLovesOnUserStories);

    }
}
