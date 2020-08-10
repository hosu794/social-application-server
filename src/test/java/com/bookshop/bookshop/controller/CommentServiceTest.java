package com.bookshop.bookshop.controller;

import com.bookshop.bookshop.model.*;
import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.CommentRequest;
import com.bookshop.bookshop.payload.StoryRequest;
import com.bookshop.bookshop.repository.CommentRepository;
import com.bookshop.bookshop.repository.LoveRepository;
import com.bookshop.bookshop.repository.StoryRepository;
import com.bookshop.bookshop.repository.UserRepository;
import com.bookshop.bookshop.security.UserPrincipal;
import com.bookshop.bookshop.service.implementation.CommentService;
import com.bookshop.bookshop.service.implementation.CommentServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentServiceTest {

    StoryRepository storyRepository = Mockito.mock(StoryRepository.class);
    UserRepository userRepository = Mockito.mock(UserRepository.class);
    LoveRepository loveRepository = Mockito.mock(LoveRepository.class);
    CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    CommentService commentService = new CommentServiceImpl(userRepository, commentRepository, storyRepository, loveRepository);

    Topic topic;
    Story story;
    User user;
    Love love;
    UserPrincipal userPrincipal;
    Comment comment;
    Comment updatedComment;
    Instant createdAt;
    CommentRequest commentRequest;
    CommentRequest updatedCommentRequest;
    ApiResponse apiResponse;
    List<Comment> commentList;
    Pageable pageable;
    int total;
    int start;
    int end;
    PageImpl page;
    List<Comment> output;
    List<User> creators;

    @Before
    public void initialize() throws Exception {

        createdAt = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-31").toInstant();

        user = new User();
        user.setId(12l);
        user.setUsername("hosu794");
        user.setPassword("password");
        user.setName("Grzegorz Szczęsny");
        user.setEmail("hosu794@gmail.com");
        user.setCreatedAt(createdAt);
        userPrincipal = UserPrincipal.create(user);
        user.setUpdatedAt(createdAt);

        topic = new Topic();
        topic.setDescription("Topic Description");
        topic.setTitle("Topic title");
        topic.setId((long) 1);
        topic.setCreatedAt(createdAt);
        topic.setCreatedBy(user.getId());
        topic.setUpdatedAt(createdAt);
        topic.setUpdatedBy(user.getId());

        story = new Story();
        story.setCreatedBy((long) 1);
        story.setId((long) 1);
        story.setTopic(topic);
        story.setCreatedBy((long) 1);
        story.setTitle("Story Title");
        story.setBody("<p>Body</p>");
        story.setDescription("Story Description");
        story.setCreatedAt(createdAt);
        story.setUpdatedAt(createdAt);
        story.setUpdatedBy(user.getId());
        story.setCreatedBy(user.getId());

        comment = new Comment();
        comment.setBody("Random Comment Body");
        comment.setId(12l);
        comment.setUser(user);
        comment.setStory(story);
        comment.setCreatedAt(createdAt);
        comment.setCreatedBy(user.getId());
        comment.setUpdatedAt(createdAt);
        comment.setUpdatedBy(user.getId());

        updatedComment = comment;
        updatedComment.setBody("Updated Body");

        commentRequest = new CommentRequest();
        commentRequest.setBody(story.getBody());
        commentRequest.setStoryId(story.getId());

        updatedCommentRequest = commentRequest;
        updatedCommentRequest.setBody("Updated Body");


        apiResponse = new ApiResponse(true, "Mock Message");

         pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

         commentList = new ArrayList<>();
         commentList.add(comment);

         output = new ArrayList<>();
         output.add(comment);

         total = commentList.size();
         start = Math.toIntExact(pageable.getOffset());
         end = Math.min(start, end);
         page = new PageImpl<>(output, pageable, total);

         creators = new ArrayList<>();
         creators.add(user);



    }
    
    @Test
    public void should_return_create_comment() throws Exception {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(storyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(story));
        Mockito.when(commentRepository.save(ArgumentMatchers.any(Comment.class))).thenReturn(comment);

        Assert.assertEquals(commentService.createComment(commentRequest, userPrincipal, story.getId()).getBody(), comment.getBody());

    }

    @Test
    public void should_return_delete_comment() throws Exception {
        Mockito.when(commentRepository.findById(ArgumentMatchers.any(Long.class
        ))).thenReturn(Optional.of(comment));

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));

        Assert.assertEquals(commentService.deleteComment(comment.getId(), userPrincipal).hasBody(), apiResponse.getSuccess());
    }

    @Test
    public void should_return_getCommentById() throws Exception {
        Mockito.when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(comment));
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.anyLong())).thenReturn(12l);

        Assert.assertEquals(commentService.getCommentById(comment.getId()).getBody(), comment.getBody());
        Assert.assertEquals(commentService.getCommentById(comment.getId()).getCreatedBy().getId(), comment.getCreatedBy());
        Assert.assertEquals(commentService.getCommentById(comment.getId()).getStoryResponse().getDescription(), comment.getStory().getDescription());

    }

    @Test
    public void should_return_getAllComment() throws Exception {
        Mockito.when(commentRepository.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userRepository.findByIdIn(ArgumentMatchers.any(List.class))).thenReturn(creators);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.anyLong())).thenReturn(12l);

        Assert.assertTrue(commentService.getAllComment(0, 10).getContent().get(0).getBody().contains(comment.getBody()));
        Assert.assertTrue(commentService.getAllComment(0, 10).getContent().get(0).getCreatedBy().getUsername().contains(user.getUsername()));
        Assert.assertTrue(commentService.getAllComment(0, 10).getContent().get(0).getStoryResponse().getBody().contains(story.getBody()));


    }

    @Test
    public void should_return_getCommentsByUserId() throws Exception {

        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(commentRepository.findByUserId(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.anyLong())).thenReturn(123l);



        Assert.assertTrue(commentService.getCommentsByUserId(user.getId(), 0, 10).getContent().get(0).getBody().contains(comment.getBody()));
        Assert.assertTrue(commentService.getCommentsByUserId(user.getId(), 0, 10).getContent().get(0).getStoryResponse().getBody().contains(story.getBody()));
        Assert.assertTrue(commentService.getCommentsByUserId(user.getId(), 0, 10).getContent().get(0).getCreatedBy().getUsername().contains(user.getUsername()));

    }

    @Test
    public void should_return_getCommentsByStoryId() throws Exception {
        Mockito.when(commentRepository.findByStoryId(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userRepository.findByIdIn(ArgumentMatchers.anyList())).thenReturn(creators);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.anyLong())).thenReturn(123l);

        Assert.assertTrue(commentService.getCommentsByStoryId(story.getId(), 0, 10).getContent().get(0).getBody().contains(comment.getBody()));
        Assert.assertTrue(commentService.getCommentsByStoryId(story.getId(), 0, 10).getContent().get(0).getId().equals(comment.getId()));
        Assert.assertTrue(commentService.getCommentsByStoryId(story.getId(), 0, 10).getContent().get(0).getStoryResponse().getDescription().contains(story.getDescription()));

    }

    @Test
    public void should_return_getCommentsByCreatedBy() throws Exception {
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString())).thenReturn(Optional.of(user));
        Mockito.when(commentRepository.findByUserId(ArgumentMatchers.anyLong(), ArgumentMatchers.isA(Pageable.class))).thenReturn(page);
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.anyLong())).thenReturn(123l);

        Assert.assertTrue(commentService.getCommentsByCreatedBy(user.getUsername(), 0, 10).getContent().get(0).getBody().contains(comment.getBody()));
        Assert.assertTrue(commentService.getCommentsByCreatedBy(user.getUsername(), 0, 10).getContent().get(0).getId().equals(comment.getId()));
        Assert.assertTrue(commentService.getCommentsByCreatedBy(user.getUsername(), 0, 10).getContent().get(0).getStoryResponse().getDescription().contains(story.getDescription()));

    }

    @Test
    public void should_return_updateComment() throws Exception {
        Mockito.when(commentRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(comment));
        Mockito.when(userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(storyRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(story));
        Mockito.when(commentRepository.save(ArgumentMatchers.any(Comment.class))).thenReturn(updatedComment);
        Mockito.when(userRepository.findById(ArgumentMatchers.any(Long.class))).thenReturn(Optional.of(user));
        Mockito.when(loveRepository.countByStoryId(ArgumentMatchers.anyLong())).thenReturn(124L);


        Assert.assertTrue(commentService.updateComment(updatedCommentRequest, comment.getId(), story.getId(), userPrincipal).getId().equals(updatedComment.getId()));
        Assert.assertTrue(commentService.updateComment(updatedCommentRequest, comment.getId(), story.getId(), userPrincipal).getCreatedBy().getUsername().contains(user.getUsername()));
        Assert.assertTrue(commentService.updateComment(updatedCommentRequest, comment.getId(), story.getId(), userPrincipal).getBody().contains(updatedComment.getBody()));
    }




}
