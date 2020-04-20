package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.model.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story, Long> {

    Optional<Story> findById(Long pollId);

    Page<Story> findByCreatedBy(Long userId, Pageable pageable);

    long countByCreatedBy(Long userId);

    List<Story> findByIdIn(List<Long> pollIds);

    List<Story> findByIdIn(List<Long> pollIds, Sort sort);

    Page<Story> findByTopicId(Long topicId, Pageable pageable);

}
