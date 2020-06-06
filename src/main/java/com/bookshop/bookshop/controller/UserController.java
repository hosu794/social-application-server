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
import com.bookshop.bookshop.service.implementation.UserServiceImpl;
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
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return  userService.getCurrentUser(currentUser);
    }

    @PostMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestBody UsernameRequest usernameRequest) {

        return userService.checkUsernameAvailability(usernameRequest);

    }

    @PostMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestBody EmailRequest emailRequest) {
        return userService.checkEmailAvailability(emailRequest);
    }

    @PostMapping("/user/checkLoveAvailability")
    public LoveAvailability checkLoveAvailability(@RequestBody StoryLikedRequest storyLikedRequest) {
        return userService.checkIsUserLovedStory(storyLikedRequest.getStoryId(), storyLikedRequest.getUserId());
    }

    @GetMapping("/users/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        return userService.getUserProfile(username);
    }

    @GetMapping("/users/{username}/stories")
    public PagedResponse<StoryResponse> getStoriesCreatedBy(@PathVariable(value = "username") String username,
                                                            @CurrentUser UserPrincipal currentUser,
                                                            @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                            @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return userService.getStoriesCreatedBy(username, currentUser, page, size);
    }

    @GetMapping("/users/{username}/loves")
    public PagedResponse<StoryResponse> getStoriesLovedBy(@PathVariable(value = "username") String username,
                                                         @CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return userService.getStoriesLovedBy(username, currentUser, page,size);
    }



}
