package com.bookshop.bookshop.model;

import com.bookshop.bookshop.model.audit.UserDateAudit;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@Table(name = "topics")
public class Topic extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long id;

    @Column(name = "title", nullable = false)
    @Length(min = 5, message = "*Your title must at least 5 characters")
    @NotEmpty(message = "*Please provide your title")
    private String title;

    @Column(name = "description", nullable = false)
    @Length(min = 5, message = "*Your description must at least 5 characters")
    @NotEmpty(message = "*Please provide your description")
    private String description;

    @OneToMany(mappedBy = "topic", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Story> stories;

    public Topic() {}

    public Topic(String title, String description) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Story> getStories() {
        return stories;
    }

    public void setStories(Set<Story> stories) {
        this.stories = stories;
    }
}
