package com.bookshop.bookshop.payload;

import com.bookshop.bookshop.model.Comment;
import com.bookshop.bookshop.model.Topic;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

public class StoryResponse {

    private Long id;
    private String title;
    private String body;
    private String description;
    private Collection<Comment> comments;

    private UserSummary createdBy;
    private Instant creationDateTime;

    private TopicResponse topic;

    private Long totalLoves;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public UserSummary getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserSummary createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public Long getTotalLoves() {
        return totalLoves;
    }

    public void setTotalLoves(Long totalLoves) {
        this.totalLoves = totalLoves;
    }

    public TopicResponse getTopic() {
        return topic;
    }

    public void setTopic(TopicResponse topic) {
        this.topic = topic;
    }
}
