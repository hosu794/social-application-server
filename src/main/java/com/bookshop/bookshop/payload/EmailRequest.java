package com.bookshop.bookshop.payload;

public class EmailRequest {

    private String email;

    public EmailRequest() {}

    public EmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
