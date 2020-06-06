package com.bookshop.bookshop.payload;

public class StoryLikedRequest {

    private Long storyId;
    private Long userId;

    public StoryLikedRequest() {}

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
