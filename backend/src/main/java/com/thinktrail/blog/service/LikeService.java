package com.thinktrail.blog.service;

import com.thinktrail.blog.model.Like;
import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import java.util.List;

public interface LikeService {
    Like createLike(Like like);
    Like getLikeById(Long id);
    Like existsByUserIdAndPostId(Long userId, Long postId);
    List<Like> getLikesByUser(User user);
    List<Like> getLikesByPost(Post post);
    long countByPostId(Long postId);
    void deleteLike(Like like);
    void deleteLikeById(Long id);
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
