package com.bookshop.bookshop.model;

import com.bookshop.bookshop.model.audit.DateAudit;
import com.bookshop.bookshop.model.audit.UserDateAudit;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "stories")
public class Story extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long id;

    @Column(name = "title", nullable = false)
    @Length(min = 5, message = "*Your title must at least 5 characters")
    @NotEmpty(message = "*Please provide title")
    private String title;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "description", nullable = false)
    @NotEmpty(message = "*Please provide the description")
    private String description;

    @OneToMany(mappedBy = "story", cascade = CascadeType.REMOVE)
    private Collection<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Love> loves;

    public Story(@Length(min = 5, message = "*Your title must at least 5 characters") @NotEmpty(message = "*Please provide title") String title,
                 String body, @NotEmpty(message = "*Please provide the description") String description, Topic topic) {
        this.title = title;
        this.body = body;
        this.description = description;
        this.topic = topic;
    }

    public Story(@Length(min = 5, message = "*Your title must at least 5 characters") @NotEmpty(message = "*Please provide title") String title,
                 String body, @NotEmpty(message = "*Please provide the description") String description) {
        this.title = title;
        this.body = body;
        this.description = description;
    }

    public Story() {}



    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }
}
