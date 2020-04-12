package com.bookshop.bookshop.model;

public class StoryLoveCount {

    private Long storyId;
    private Long loveCount;

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }

    public Long getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(Long loveCount) {
        this.loveCount = loveCount;
    }

    public StoryLoveCount(Long storyId, Long loveCount) {
        this.storyId = storyId;
        this.loveCount = loveCount;
    }
}
