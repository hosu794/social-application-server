package com.bookshop.bookshop.payload;

public class UpdateNameRequest {

    private String name;

    public UpdateNameRequest(String name) {
        this.name = name;
    }

    public UpdateNameRequest() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
