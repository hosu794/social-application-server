package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByStoryId(Long storyId, Pageable pageable);

    Page<Comment> findByUserId(Long userId, Pageable pageable);

}
