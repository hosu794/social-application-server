package com.bookshop.bookshop.payload;

import com.bookshop.bookshop.model.Story;

import java.time.Instant;
import java.util.Set;

public class TopicResponse {

    private Long id;
    private String title;
    private String description;
    private Instant createdAt;
    private UserSummary createdBy;

    public TopicResponse() {}

    public TopicResponse(Long id, String title, String description, Instant createdAt, UserSummary createdBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getcreatedAt() {
        return createdAt;
    }

    public void setcreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserSummary createdBy) {
        this.createdBy = createdBy;
    }
}
