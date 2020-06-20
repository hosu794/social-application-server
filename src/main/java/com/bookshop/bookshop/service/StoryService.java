package com.bookshop.bookshop.service;

import com.bookshop.bookshop.model.Love;
import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.User;
import com.bookshop.bookshop.payload.ApiResponse;
import com.bookshop.bookshop.payload.PagedResponse;
import com.bookshop.bookshop.payload.StoryRequest;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface StoryService {

    public PagedResponse<StoryResponse> getAllStories(UserPrincipal currentUser, int page, int size);

    public PagedResponse<StoryResponse> getStoriesCreatedBy(String username, UserPrincipal currentUser, int page, int size);

    public PagedResponse<StoryResponse> getStoriesLovedBy(String username, UserPrincipal currentUser, int page, int size);

    public Story createStory(StoryRequest storyRequest, Long topicId);

    public StoryResponse getStoryById(Long storyId, UserPrincipal currentUser);

    public StoryResponse castLoveAndGetUpdateStory(Long topicId, UserPrincipal currentUser);

    public PagedResponse<StoryResponse> getStoryByTopicId(Long topicId, UserPrincipal currentUser, int page, int size);

    public StoryResponse deleteAndReturnNewStory(Story story, Love love, User creator);

    public Map<Long, User> getCreatorsIdsAndCreatorOfStories(List<Story> stories);

    public Map<Long, Long> getCurrentUserStoryIds(UserPrincipal currentUser, List<Long> storyIds);

    public PagedResponse<StoryResponse> getStoriesByTitle(String title, UserPrincipal currentUser, int page, int size);

    public StoryResponse deleteLoveAndGetUpdateStory(Long userId, UserPrincipal currentUser);

    public ResponseEntity<?> deleteStory(Long storyId, UserPrincipal currentUser);
}