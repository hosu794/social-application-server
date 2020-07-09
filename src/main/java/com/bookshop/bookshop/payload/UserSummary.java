package com.bookshop.bookshop.payload;

public class UserSummary {

    private Long id;
    private String username;
    private String name;
    private String avatarDownloadUri;

    public String getAvatarDownloadUri() {
        return avatarDownloadUri;
    }

    public void setAvatarDownloadUri(String avatarDownloadUri) {
        this.avatarDownloadUri = avatarDownloadUri;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserSummary(Long id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public UserSummary(Long id, String username, String name, String avatarDownloadUri) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatarDownloadUri = avatarDownloadUri;
    }
}
