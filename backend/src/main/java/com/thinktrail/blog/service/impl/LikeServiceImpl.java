package com.thinktrail.blog.service.impl;

import com.thinktrail.blog.model.Like;
import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.repository.LikeRepository;
import com.thinktrail.blog.repository.PostRepository;
import com.thinktrail.blog.service.LikeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    @Override
    public Like createLike(Like like) {
        Like savedLike = likeRepository.save(like);
        // Increment post likes count
        postRepository.incrementLikes(like.getPost().getId());
        return savedLike;
    }

    @Override
    public void deleteByUserIdAndPostId(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId);
        if (like != null) {
            likeRepository.delete(like);
            // Decrement post likes count
            postRepository.decrementLikes(postId);
        }
    }

    @Override
    public Like getLikeById(Long id) {
        return likeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Like not found with ID: " + id));
    }

    @Override
    public List<Like> getLikesByUser(User user) {
        return likeRepository.findByUserId(user.getId());
    }

    @Override
    public List<Like> getLikesByPost(Post post) {
        return likeRepository.findByPostId(post.getId());
    }

    @Override
    public Like existsByUserIdAndPostId(Long userId, Long postId) {
        return likeRepository.findByUserIdAndPostId(userId, postId);
    }

    @Override
    public long countByPostId(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public void deleteLike(Like like) {
        likeRepository.delete(like);
        postRepository.decrementLikes(like.getPost().getId());
    }

    @Override
    public void deleteLikeById(Long id) {
        Like like = likeRepository.findById(id).orElse(null);
        if (like != null) {
            likeRepository.deleteById(id);
            postRepository.decrementLikes(like.getPost().getId());
        }
    }
}