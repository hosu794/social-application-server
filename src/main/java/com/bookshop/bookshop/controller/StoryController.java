package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.CurrentUser;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.implementation.StoryServiceImpl;
import com.bookshop.bookshop.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private LoveRepository loveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoryServiceImpl storyService;

    private static final Logger logger = LoggerFactory.getLogger(StoryController.class);

    @GetMapping
    public PagedResponse<StoryResponse> getStories(@CurrentUser UserPrincipal currentUser,
                                                   @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                   @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return storyService.getAllStories(currentUser, page, size);
    }

    @PostMapping("/topics/{topicId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createStory(@Valid @RequestBody StoryRequest storyRequest, @PathVariable Long topicId) {
        Story story = storyService.createStory(storyRequest, topicId);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{storyId}")
                .buildAndExpand(story.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Story Created Successfully"));
    }

    @GetMapping("/{storyId}")
    public StoryResponse getStoryById(@CurrentUser UserPrincipal currentUser, @PathVariable Long storyId) {
        return storyService.getStoryById(storyId, currentUser);
    }

    @GetMapping("/title/{title}")
    public PagedResponse<StoryResponse> getStoriesByTitle(@CurrentUser UserPrincipal currentUser, @PathVariable String title,
                                                          @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return storyService.getStoriesByTitle(title, currentUser, page, size);
    }

    @GetMapping("/topics/{topicId}")
    public PagedResponse<StoryResponse> getStoryByTopicId(@PathVariable Long topicId, @CurrentUser UserPrincipal currentUser,
                                                          @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                          @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return storyService.getStoryByTopicId(topicId, currentUser, page, size);
    }

    @PostMapping("/loves")
    @PreAuthorize("hasRole('USER')")
    public StoryResponse castLove(@CurrentUser UserPrincipal currentUser, @RequestBody LoveRequest loveRequest) {
        return storyService.castLoveAndGetUpdateStory(loveRequest.getStoryId(), currentUser);
    }




}
