package com.thinktrail.blog.service;

import com.thinktrail.blog.model.Like;
import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.model.Comment;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User getUserById(Long id);
    List<User> getAllUsers();
    List<Post> getAllPostsByUser(Long userId);
    List<Like> getAllLikesByUser(Long userId);
    List<Comment> getAllCommentsByUser(Long userId);
    void deleteUser(User user);
    void deleteUserById(Long id);
    User followUser(Long followerId, Long followeeId);
    User unfollowUser(Long followerId, Long followeeId);
}
