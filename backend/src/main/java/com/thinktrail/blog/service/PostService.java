package com.thinktrail.blog.service;

import com.thinktrail.blog.model.Post;

import java.util.List;

public interface PostService {
    Post createPost(Post post);
    List<Post> getPostsByUserId(Long userId);
    Post getPostById(Long id);
    List<Post> getPostByTitleContaining(String keyword);
    List<Post> getPostBySummaryContaining(String keyword);
    List<Post> getAllPosts();
    void deletePost(Post post);
    void deletePostById(Long id);
}