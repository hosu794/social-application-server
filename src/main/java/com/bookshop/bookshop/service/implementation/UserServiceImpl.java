package com.bookshop.bookshop.service.implementation;

import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.StoryService;
import com.bookshop.bookshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    public UserServiceImpl(UserRepository userRepository, StoryRepository storyRepository, LoveRepository loveRepository, StoryService storyService) {
        this.userRepository = userRepository;
        this.storyRepository = storyRepository;
        this.loveRepository = loveRepository;
        this.storyService = storyService;
    }


    UserRepository userRepository;
    StoryRepository storyRepository;
    LoveRepository loveRepository;
    StoryService storyService;

    @Override
    public UserSummary getCurrentUser(UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(),currentUser.getUsername(), currentUser.getName());
        return userSummary;
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
}
