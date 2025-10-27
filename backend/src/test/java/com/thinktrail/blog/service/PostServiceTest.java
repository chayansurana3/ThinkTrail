package com.thinktrail.blog.service;

import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.model.Like;
import com.thinktrail.blog.repository.PostRepository;
import com.thinktrail.blog.repository.UserRepository;
import com.thinktrail.blog.service.impl.PostServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @InjectMocks private PostServiceImpl postService;

    @Test
    void createPostTest() {
        Post post = new Post();
        LocalDateTime createdAt = LocalDateTime.now();
        post.setTitle("Test Post");
        post.setContent("This is the test content. Pls ignore!!");
        post.setId(1L);
        post.setLive(true);
        post.setShares(12L);
        post.setLikes(15L);
        post.setUser(user);
        post.setCreatedAt(createdAt);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post newPost = postService.createPost(post);

        assertEquals("Test Post", newPost.getTitle());
        assertEquals(1L, newPost.getId());
        assertEquals("This is the test content. Pls ignore!!", newPost.getContent());
        assertEquals(15L, newPost.getLikes());
        assertEquals(12L, newPost.getShares());
        assertEquals(user, newPost.getUser());
        assertEquals(createdAt, newPost.getCreatedAt());
        assertTrue(newPost.isLive());

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void getPostsByUserIdTest() {
        Long userId = 1L;

        Post post1 = new Post();
        post1.setId(101L);
        post1.setTitle("First Post");

        Post post2 = new Post();
        post2.setId(102L);
        post2.setTitle("Second Post");

        List<Post> posts = Arrays.asList(post1, post2);

        User user = new User();
        user.setId(userId);
        user.setPosts(posts);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<Post> result = postService.getPostsByUserId(userId);

        assertEquals(2, result.size());
        assertEquals("First Post", result.get(0).getTitle());
        assertEquals("Second Post", result.get(1).getTitle());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getPostsByUserId_UserNotFound() {
        Long userId = 99L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.getPostsByUserId(userId);
        });

        assertTrue(exception.getMessage().contains("User not found"));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getPostById_found() {
        Long postId = 1L;

        Post post = new Post();
        post.setTitle("Test Post");
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        Post getPost = postService.getPostById(postId);

        assertEquals("Test Post", getPost.getTitle());
        assertEquals(1L, getPost.getId());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void getPostById_notFound() {
        Long postId = 999L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.getPostById(postId);
        });

        assertTrue(exception.getMessage().contains("Post not found"));
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void getPostByTitleContaining_resultsFound() {
        String phrase = "Title";

        Post post1 = new Post();
        post1.setTitle("Post Title 1");

        Post post2 = new Post();
        post2.setTitle("Post Title 2");

        List<Post> mockResult = Arrays.asList(post1, post2);

        when(postRepository.findByTitleContaining(phrase)).thenReturn(mockResult);

        List<Post> result = postService.getPostByTitleContaining(phrase);

        assertEquals(2, result.size());
        assertTrue(result.get(0).getTitle().contains("Title"));
        assertTrue(result.get(1).getTitle().contains("Title"));

        verify(postRepository, times(1)).findByTitleContaining(phrase);
    }

    @Test
    void getPostByTitleContaining_emptyResult() {
        String phrase = "NoMatch";

        when(postRepository.findByTitleContaining(phrase)).thenReturn(Collections.emptyList());

        List<Post> result = postService.getPostByTitleContaining(phrase);

        assertTrue(result.isEmpty());
        verify(postRepository, times(1)).findByTitleContaining(phrase);
    }

    @Test
    void deletePost() {
        Post post = new Post();
        postService.deletePost(post);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void deletePostById() {
        Long postId = 1L;

        postService.deletePostById(postId);

        verify(postRepository, times(1)).deleteById(postId);
    }
}
