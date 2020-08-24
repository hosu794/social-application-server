package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.Topic;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.PagedResponse;
import com.bookshop.bookshop.payload.TopicRequest;
import com.bookshop.bookshop.payload.TopicResponse;
import com.bookshop.bookshop.security.CurrentUser;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.TopicService;
import com.bookshop.bookshop.service.implementation.TopicServiceImpl;
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
@RequestMapping("/api/topics")
public class TopicController {

    private static final Logger logger = LoggerFactory.getLogger(TopicController.class);


    public TopicController(TopicServiceImpl topicService) {
        this.topicService =topicService;
    }
    private final TopicServiceImpl topicService;

    @GetMapping
    public PagedResponse<TopicResponse> getAllTopics(@CurrentUser UserPrincipal currentUser,
                                                     @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return topicService.getAllTopics(currentUser, page, size);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createStory(@Valid @RequestBody TopicRequest topicRequest) {
        Topic topic = topicService.createTopic(topicRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{topicId")
                .buildAndExpand(topic.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Topic Created Successfully"));
    }

    @GetMapping("/title/{title}")
    public TopicResponse getTopicByTitle(@CurrentUser UserPrincipal currentUser, @PathVariable String title) {
        return topicService.getTopicByTitle(title, currentUser);


    }


    @GetMapping("/{topicId}")
    public TopicResponse getTopicById(@CurrentUser UserPrincipal currentUser, @PathVariable Long topicId) {
        return topicService.getTopicById(topicId, currentUser);
    }

}
