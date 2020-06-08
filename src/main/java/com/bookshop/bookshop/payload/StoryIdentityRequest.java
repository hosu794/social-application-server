package com.bookshop.bookshop.payload;

public class StoryIdentityRequest {

    private Long storyId;

    public StoryIdentityRequest(Long storyId) {
        this.storyId = storyId;
    }

    public StoryIdentityRequest() {}

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }
}
