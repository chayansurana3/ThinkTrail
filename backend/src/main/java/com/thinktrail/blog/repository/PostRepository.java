package com.thinktrail.blog.repository;

import com.thinktrail.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);
    List<Post> findByTitleContaining(String keyword);
    List<Post> findBySummaryContaining(String keyword);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likes = p.likes + 1 WHERE p.id = :postId")
    void incrementLikes(@Param("postId") Long postId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p SET p.likes = p.likes - 1 WHERE p.id = :postId AND p.likes > 0")
    void decrementLikes(@Param("postId") Long postId);
}