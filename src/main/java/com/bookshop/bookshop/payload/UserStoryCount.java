package com.bookshop.bookshop.payload;

public class UserStoryCount {

    private long storiesCreated;
    private long storiesLiked;
    private long lovesOnCreatedStories;

    public UserStoryCount() {

    }




    public long getStoriesCreated() {
        return storiesCreated;
    }

    public void setStoriesCreated(long storiesCreated) {
        this.storiesCreated = storiesCreated;
    }

    public long getStoriesLiked() {
        return storiesLiked;
    }

    public void setStoriesLiked(long storiesLiked) {
        this.storiesLiked = storiesLiked;
    }

    public long getLovesOnCreatedStories() {
        return lovesOnCreatedStories;
    }

    public void setLovesOnCreatedStories(long lovesOnCreatedStories) {
        this.lovesOnCreatedStories = lovesOnCreatedStories;
    }

    public UserStoryCount(long storiesCreated, long storiesLiked, long lovesOnCreatedStories) {
        this.storiesCreated = storiesCreated;
        this.storiesLiked = storiesLiked;
        this.lovesOnCreatedStories = lovesOnCreatedStories;
    }
}
