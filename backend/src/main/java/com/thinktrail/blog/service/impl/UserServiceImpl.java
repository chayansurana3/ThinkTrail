package com.thinktrail.blog.service.impl;

import com.thinktrail.blog.model.User;
import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.Like;
import com.thinktrail.blog.model.Comment;
import com.thinktrail.blog.repository.UserRepository;
import com.thinktrail.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<Post> getAllPostsByUser(Long userId) {
        User user = getUserById(userId);
        return user.getPosts();
    }

    @Override
    public List<Like> getAllLikesByUser(Long userId) {
        User user = getUserById(userId);
        return user.getLikes();
    }

    @Override
    public List<Comment> getAllCommentsByUser(Long userId) {
        User user = getUserById(userId);
        return user.getComments();
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public User followUser(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("User cannot follow themselves.");
        }

        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found"));

        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new IllegalArgumentException("Followee not found"));

        if (!followee.getFollowers().contains(follower)) {
            followee.getFollowers().add(follower);
            follower.getFollowing().add(followee);
        }

        userRepository.save(follower);
        return userRepository.save(followee);
    }

    @Override
    public User unfollowUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found"));

        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new IllegalArgumentException("Followee not found"));

        if (followee.getFollowers().contains(follower)) {
            followee.getFollowers().remove(follower);
            follower.getFollowing().remove(followee);
        }

        userRepository.save(follower);
        return userRepository.save(followee);
    }
}
