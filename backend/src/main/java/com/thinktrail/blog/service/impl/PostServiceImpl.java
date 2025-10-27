package com.thinktrail.blog.service.impl;

import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.repository.PostRepository;
import com.thinktrail.blog.repository.UserRepository;
import com.thinktrail.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> getPostsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return user.getPosts();
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
    }

    @Override
    public List<Post> getPostByTitleContaining(String keyword) {
        return postRepository.findByTitleContaining(keyword);
    }

    @Override
    public List<Post> getPostBySummaryContaining(String keyword) {
        return postRepository.findBySummaryContaining(keyword);
    }

    @Override
    public List<Post> getAllPosts(){
        return  postRepository.findAll();
    }

    @Override
    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }
}