package com.bookshop.bookshop.payload;

import java.time.Instant;

public class CommentResponse {

    private Long id;
    private String body;
    private UserSummary createdBy;
    private StoryResponse storyResponse;
    private Instant creationDateTime;

    public CommentResponse(Long id, String body, UserSummary createdBy, StoryResponse storyResponse, Instant creationDateTime) {
        this.id = id;
        this.body = body;
        this.createdBy = createdBy;
        this.storyResponse = storyResponse;
        this.creationDateTime = creationDateTime;
    }

    public CommentResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserSummary createdBy) {
        this.createdBy = createdBy;
    }

    public StoryResponse getStoryResponse() {
        return storyResponse;
    }

    public void setStoryResponse(StoryResponse storyResponse) {
        this.storyResponse = storyResponse;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
