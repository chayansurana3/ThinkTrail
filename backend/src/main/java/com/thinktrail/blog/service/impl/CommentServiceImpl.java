package com.thinktrail.blog.service.impl;

import com.thinktrail.blog.model.Comment;
import com.thinktrail.blog.repository.CommentRepository;
import com.thinktrail.blog.repository.UserRepository;
import com.thinktrail.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Like not found with ID: " + id));
    }

    @Override
    public List<Comment> findCommentsByUserId(Long userId){
        return commentRepository.findByUserId(userId);
    }

    @Override
    public List<Comment> findCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public List<Comment> findByCreatedAt(LocalDateTime createdAt) {
        return commentRepository.findByCreatedAt(createdAt);
    }

    @Override
    public void deleteComment(Comment comment){
        commentRepository.delete(comment);
    }

    @Override
    public void deleteCommentById(Long id) {
        commentRepository.deleteById(id);
    }
}
