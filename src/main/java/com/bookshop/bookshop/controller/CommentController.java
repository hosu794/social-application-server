package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.Comment;
import com.bookshop.bookshop.payload.*;
import com.bookshop.bookshop.security.CurrentUser;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.implementation.CommentService;
import com.bookshop.bookshop.service.implementation.CommentServiceImpl;
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
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @GetMapping
    public PagedResponse<CommentResponse> getStories(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size ) {

        return commentService.getAllComment(page, size);

    }

    @GetMapping("/{commentId}")
    public CommentResponse getCommentById(@CurrentUser UserPrincipal currentUser, @PathVariable Long commentId) {
        return commentService.getCommentById(commentId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public CommentResponse createComment(@Valid @RequestBody CommentRequest commentRequest, @CurrentUser UserPrincipal currentUser) {
        return commentService.createComment(commentRequest, currentUser, commentRequest.getStoryId());
    }

    @GetMapping("/username/{username}")
    public PagedResponse<CommentResponse> getCommentsByCreatedBy(@PathVariable String username, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                                 @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return commentService.getCommentsByCreatedBy(username, page, size);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteComment(@CurrentUser UserPrincipal currentUser, @PathVariable Long commentId){
        return commentService.deleteComment(commentId, currentUser);
    }

    @GetMapping("/user/{userId}")
    public PagedResponse<CommentResponse> getCommentsByUserId(@PathVariable Long userId, @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {

        return commentService.getCommentsByUserId(userId, page, size);
    }

    @GetMapping("/story/{storyId}")
    public PagedResponse<CommentResponse> getCommentsByStoryId(@PathVariable Long storyId ,  @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                               @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return commentService.getCommentsByStoryId(storyId, page ,size);
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public CommentResponse updateComment(@CurrentUser UserPrincipal currentUser, @PathVariable Long commentId, @RequestBody @Valid CommentRequest commentRequest) {
        return commentService.updateComment(commentRequest, commentId, commentRequest.getStoryId(), currentUser);
    }


}
