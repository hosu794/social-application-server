package com.bookshop.bookshop.payload;

public class UpdatePasswordRequest {

    private String password;

    public UpdatePasswordRequest(String password) {
        this.password = password;
    }

    public UpdatePasswordRequest() {}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
