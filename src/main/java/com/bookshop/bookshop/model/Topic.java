package com.bookshop.bookshop.model;

import com.bookshop.bookshop.model.audit.DateAudit;
import com.bookshop.bookshop.model.audit.UserDateAudit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "topics")
public class Topic extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "topics")
    private Collection<Story> stories;

    public Topic() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Story> getStories() {
        return stories;
    }

    public void setStories(Collection<Story> stories) {
        this.stories = stories;
    }
}
