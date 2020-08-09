package com.bookshop.bookshop.service.implementation;

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

    public Comment createComment(CommentRequest commentRequest, UserPrincipal currentUser, Long storyId);

    public ResponseEntity<?> deleteComment(Long commentId, Long storyId, UserPrincipal currentUser);

    public CommentResponse getCommentById(Long commentId, UserPrincipal currentUser);

    public PagedResponse<CommentResponse> getAllComment (UserPrincipal currentUser, int page, int size);

    public PagedResponse<CommentResponse> getCommentByCreatedBy(String username, UserPrincipal currentUser, int page, int size);

    public PagedResponse<CommentResponse> getCommentByStory(Long storyId, UserPrincipal currentUser, int page, int size);

    public CommentResponse updateComment(CommentRequest commentRequest, Long commentId, UserPrincipal currentUser);

    public Map<Long, User> getCreatorsIdsAncCreatorOfComments(List<Comment> comments);

}
