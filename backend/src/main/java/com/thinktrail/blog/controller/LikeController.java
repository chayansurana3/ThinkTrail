package com.thinktrail.blog.controller;

import com.thinktrail.blog.model.Like;
import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.service.LikeService;
import com.thinktrail.blog.service.PostService;
import com.thinktrail.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> likePost(@RequestParam Long userId, @RequestParam Long postId) {
        if (likeService.existsByUserIdAndPostId(userId, postId) != null) {
            return ResponseEntity.badRequest().build(); // Already liked
        }

        User user = userService.getUserById(userId);
        Post post = postService.getPostById(postId);

        Like like = Like.builder().user(user).post(post).build();

        Like savedLike = likeService.createLike(like);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/{postId}")
    public ResponseEntity<Void> isPostLikedByUser(@PathVariable Long userId, @PathVariable Long postId) {
        if (likeService.existsByUserIdAndPostId(userId, postId) != null) {
            return ResponseEntity.ok().build(); // Liked
        }
        return ResponseEntity.notFound().build(); // Not liked
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Like>> getLikesByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(likeService.getLikesByUser(user));
    }

    @GetMapping("numberOfLikes/{postId}")
    public ResponseEntity<Number> getCountOfLikesByPost(@PathVariable Long postId){
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(likeService.countByPostId(postId));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Like>> getLikesByPost(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(likeService.getLikesByPost(post));
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikePost(@RequestParam Long userId, @RequestParam Long postId) {
        if (likeService.existsByUserIdAndPostId(userId, postId) == null) {
            return ResponseEntity.notFound().build(); // Not liked yet
        }

        likeService.deleteByUserIdAndPostId(userId, postId);
        return ResponseEntity.noContent().build();
    }
}
