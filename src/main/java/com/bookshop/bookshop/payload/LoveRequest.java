package com.bookshop.bookshop.payload;

import javax.validation.constraints.NotNull;

public class LoveRequest {

    @NotNull
    private Long storyId;

    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }
}
