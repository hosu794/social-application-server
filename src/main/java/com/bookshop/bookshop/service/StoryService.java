package com.bookshop.bookshop.service;

import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.payload.PagedResponse;
import com.bookshop.bookshop.payload.StoryRequest;
import com.bookshop.bookshop.payload.StoryResponse;
import com.bookshop.bookshop.security.UserPrincipal;

public interface StoryService {

    public PagedResponse<StoryResponse> getAllStories(UserPrincipal currentUser, int page, int size);

    public PagedResponse<StoryResponse> getStoriesCreatedBy(String username, UserPrincipal currentUser, int page, int size);

    public PagedResponse<StoryResponse> getStoriesLovedBy(String username, UserPrincipal currentUser, int page, int size);

    public Story createStory(StoryRequest storyRequest, Long topicId);

    public StoryResponse getStoryById(Long storyId, UserPrincipal currentUser);

    public StoryResponse castLoveAndGetUpdateStory(Long topicId, UserPrincipal currentUser);

    public PagedResponse<StoryResponse> getStoryByTopicId(Long topicId, UserPrincipal currentUser, int page, int size);


}