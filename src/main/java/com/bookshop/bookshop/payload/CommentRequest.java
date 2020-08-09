package com.bookshop.bookshop.payload;

public class CommentRequest {

    private String body;
    private Long storyId;

    public CommentRequest(String body, Long storyId) {
        this.body = body;

        this.storyId = storyId;
    }

    public CommentRequest() {}

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }



    public Long getStoryId() {
        return storyId;
    }

    public void setStoryId(Long storyId) {
        this.storyId = storyId;
    }
}

