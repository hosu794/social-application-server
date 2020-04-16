package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.exception.ResourceNotFoundException;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.CurrentUser;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.StoryService;
import com.bookshop.bookshop.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private LoveRepository loveRepository;

    @Autowired
    private StoryService storyService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());

        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestBody UsernameRequest usernameRequest) {
        Boolean isAvailable = !userRepository.existsByUsername(usernameRequest.getUsername());
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestBody EmailRequest emailRequest) {
        Boolean isAvailable = !userRepository.existsByEmail(emailRequest.getEmail());
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        long storyCount = storyRepository.countByCreatedBy(user.getId());
        long loveCount = loveRepository.countByUserId(user.getId());

        UserProfile userProfile = new UserProfile(user.getId(), user.getUsername(), user.getName(), user.getCreatedAt(), storyCount, loveCount);

        return userProfile;
    }

    @GetMapping("/users/{username}/stories")
    public PagedResponse<StoryResponse> getStoriesCreatedBy(@PathVariable(value = "username") String username,
                                                            @CurrentUser UserPrincipal currentUser,
                                                            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return storyService.getStoriesCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/users/{username}/loves")
    public PagedResponse<StoryResponse> getStoriesLoveBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return storyService.getStoriesLovedBy(username, currentUser, page, size);
    }



}
