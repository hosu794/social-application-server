package com.bookshop.bookshop.model;

import com.bookshop.bookshop.model.audit.DateAudit;
import com.bookshop.bookshop.model.audit.UserDateAudit;

import javax.persistence.*;

@Entity
@Table(name = "loves", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "user_id",
                "story_id"
        })
})
public class Love extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "story_id" ,nullable = false)
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Love(Story story, User user) {
        this.story = story;
        this.user = user;
    }

    public Love() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
