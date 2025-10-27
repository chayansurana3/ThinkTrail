package com.thinktrail.blog.service;

import com.thinktrail.blog.model.Comment;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    Comment createComment(Comment comment);
    Comment findCommentById(Long id);
    List<Comment> findCommentsByUserId(Long userId);
    List<Comment> findCommentsByPostId(Long userId);
    List<Comment> findByCreatedAt(LocalDateTime createdAt);
    void deleteComment(Comment comment);
    void deleteCommentById(Long id);
}
