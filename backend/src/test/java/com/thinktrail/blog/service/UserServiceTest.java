package com.thinktrail.blog.service;

import com.thinktrail.blog.model.*;
import com.thinktrail.blog.repository.UserRepository;
import com.thinktrail.blog.service.impl.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser() {
        User user = new User();
        user.setId(1L);
        user.setAge(22);
        user.setBio("I am a human!");
        user.setEmail("chayan.surana3@gmail.com");
        user.setGender("Male");
        user.setFirstName("Chayan");
        user.setMiddleName("");
        user.setLastName("Surana");
        user.setPassword("***");
        user.setRole(Role.USER);

        when(userRepository.save(any(User.class))).thenReturn(user);

        User saved = userService.createUser(user);

        assertEquals(1L, saved.getId());
        assertEquals("Chayan", saved.getFirstName());
        assertEquals("***", saved.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void getUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User found = userService.getUserById(userId);

        assertEquals(userId, found.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllUsers() {
        User user1 = new User();
        User user2 = new User();

        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> found = userService.getAllUsers();

        assertEquals(2, found.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getAllPostsByUser() {
        Long userId = 1L;
        User user = new User();
        Post post1 = new Post();
        post1.setTitle("First Post");
        Post post2 = new Post();
        post2.setTitle("Second Post");

        user.setPosts(Arrays.asList(post1, post2));
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<Post> posts = userService.getAllPostsByUser(userId);

        assertEquals(2, posts.size());
        assertEquals("First Post", posts.getFirst().getTitle());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllLikesByUser() {
        Long userId = 1L;
        User user = new User();
        Like like1 = new Like();
        Like like2 = new Like();

        user.setLikes(Arrays.asList(like1, like2));
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<Like> likes = userService.getAllLikesByUser(userId);

        assertEquals(2, likes.size());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getAllCommentsByUser() {
        Long userId = 1L;
        User user = new User();
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();

        user.setComments(Arrays.asList(comment1, comment2));
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        List<Comment> comments = userService.getAllCommentsByUser(userId);

        assertEquals(2, comments.size());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void deleteUser(){
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        userService.deleteUser(user);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void deleteUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}