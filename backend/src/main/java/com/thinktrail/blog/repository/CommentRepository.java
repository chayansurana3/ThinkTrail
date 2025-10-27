package com.thinktrail.blog.repository;

import com.thinktrail.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserId(Long userId);
    List<Comment> findByPostId(Long postId);
    List<Comment> findByCreatedAt(LocalDateTime createdAt);
}