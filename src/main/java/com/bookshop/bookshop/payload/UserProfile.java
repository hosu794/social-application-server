package com.bookshop.bookshop.payload;

import java.time.Instant;

public class UserProfile {

    private Long id;

    private String username;
    private String name;

    private Instant joinedAt;

    private Long storyCount;

    private Long loveCount;

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

    public Instant getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Instant joinedAt) {
        this.joinedAt = joinedAt;
    }

    public Long getStoryCount() {
        return storyCount;
    }

    public void setStoryCount(Long storyCount) {
        this.storyCount = storyCount;
    }

    public Long getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(Long loveCount) {
        this.loveCount = loveCount;
    }

    public UserProfile(Long id, String username, String name, Instant joinedAt, Long storyCount, Long loveCount) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.storyCount = storyCount;
        this.loveCount = loveCount;
    }

    public UserProfile(Long id, String username, String name, Instant joinedAt, Long storyCount, Long loveCount, String avatarDownloadUri) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.joinedAt = joinedAt;
        this.storyCount = storyCount;
        this.loveCount = loveCount;
        this.avatarDownloadUri = avatarDownloadUri;
    }
}
