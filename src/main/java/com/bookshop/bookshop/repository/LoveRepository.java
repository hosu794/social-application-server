package com.bookshop.bookshop.repository;

import com.bookshop.bookshop.model.Love;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoveRepository extends JpaRepository<Love, Long> {

    @Query("SELECT count(*) FROM Love v WHERE v.story.id = :storyId")
    long countByStoryId(@Param("storyId") Long storyId);

    @Query("SELECT v FROM Love v where v.user.id = :userId and v.story.id in :storyIds")
    List<Love> findByUserIdAndStoryIdIn(@Param("userId") Long userId, @Param("storyIds") List<Long> storyIds);

    @Query("SELECT v FROM Love v where v.user.id = :userId and v.story.id in :storyId")
    Love findByUserIdAndStoryId(@Param("userId") Long userId, @Param("storyId") Long storyId);

    @Query("SELECT COUNT(v.id) from Love v where v.user.id = :userId")
    long countByUserId(@Param("userId")Long userId);

    @Query("SELECT v.story.id FROM Love v where v.user.id = :userId")
    Page<Long> findLoveStoryIdsByUserId(@Param("userId") Long userId, Pageable pageable);

}
