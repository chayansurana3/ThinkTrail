package com.thinktrail.blog.controller;

import com.thinktrail.blog.dto.CommentDTO;
import com.thinktrail.blog.model.Comment;
import com.thinktrail.blog.model.ModerationResult;
import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.service.CommentService;
import com.thinktrail.blog.service.PostService;
import com.thinktrail.blog.service.UserService;

import com.thinktrail.blog.service.impl.ModerationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final ModerationServiceImpl moderationService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByPostId(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        List<CommentDTO> comments = commentService.findCommentsByPostId(postId)
                .stream()
                .map(CommentDTO::fromEntity)
                .toList();

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByUserId(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> comments = user.getComments();
        List<CommentDTO> commentDTOs = comments.stream().map(CommentDTO::fromEntity).toList();

        return ResponseEntity.ok(commentDTOs);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        User user = userService.getUserById(commentDTO.getUserId());
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid user ID"));
        }

        Post post = postService.getPostById(postId);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Post not found"));
        }

        Comment comment = Comment.builder()
                .content(commentDTO.getContent())
                .user(user)
                .post(post)
                .build();

        ModerationResult result = moderationService.moderateText(comment.getContent());

        if (result.getError() != null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Moderation failed", "details", result.getError()));
        }

        if (result.isFlagged()) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("error", result.getReason()));
        }

        Comment saved = commentService.createComment(comment);
        return ResponseEntity.ok(CommentDTO.fromEntity(saved));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        Comment comment = commentService.findCommentById(commentId);
        if (comment == null) {
            return ResponseEntity.notFound().build();
        }

        commentService.deleteComment(comment);
        return ResponseEntity.noContent().build();
    }
}