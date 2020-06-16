package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.model.Story;
import com.bookshop.bookshop.model.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    Page<Topic> findByCreatedBy(Long userId, Pageable pageable);

    Optional<Topic> findByTitle(String title);

}
