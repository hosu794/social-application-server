package com.bookshop.bookshop.payload;

public class CommentRequest {

    private String body;

    public CommentRequest(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
