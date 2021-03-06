package com.bookshop.bookshop.service;

import com.bookshop.bookshop.model.Comment;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.CommentRequest;
import com.bookshop.bookshop.payload.CommentResponse;
import com.bookshop.bookshop.payload.PagedResponse;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CommentService {

    public CommentResponse createComment(CommentRequest commentRequest, UserPrincipal currentUser, Long storyId);

    public ResponseEntity<?> deleteComment(Long commentId, UserPrincipal currentUser);

    public CommentResponse getCommentById(Long commentId);

    public PagedResponse<CommentResponse> getAllComment (int page, int size);

    public PagedResponse<CommentResponse> getCommentsByCreatedBy(String username, int page, int size);

    public PagedResponse<CommentResponse> getCommentsByUserId(Long userId, int page, int size);

    public PagedResponse<CommentResponse> getCommentsByStoryId(Long storyId, int page, int size);

    public CommentResponse updateComment(CommentRequest commentRequest, Long commentId, Long storyId, UserPrincipal currentUser);



}
