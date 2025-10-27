package com.thinktrail.blog.controller;

import com.thinktrail.blog.dto.PostDTO;
import com.thinktrail.blog.model.ModerationResult;
import com.thinktrail.blog.request.CreatePostRequest;
import com.thinktrail.blog.response.CreatePostResponse;
import com.thinktrail.blog.response.PostsResponse;
import com.thinktrail.blog.model.Post;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.service.PostService;
import com.thinktrail.blog.service.UserService;
import com.thinktrail.blog.service.impl.ModerationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final ModerationServiceImpl moderationService;

    private PostDTO convertToDTO(Post post) {
        PostDTO dto = new PostDTO();
        User user = post.getUser();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setSummary(post.getSummary());
        dto.setImageUrl(post.getImageUrl());
        dto.setLikes(post.getLikes());
        dto.setShares(post.getShares());
        dto.setLive(post.isLive());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setAuthorId(user.getId());
        dto.setAuthorName(user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName());
        return dto;
    }

    private Post convertToEntity(PostDTO dto) {
        Post post = new Post();
        User user = userService.getUserById(dto.getAuthorId());
        if (dto.getId() != null) post.setId(dto.getId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setSummary(dto.getSummary());
        post.setImageUrl(dto.getImageUrl());
        if (dto.getLikes() != null) post.setLikes(dto.getLikes());
        if (dto.getShares() != null) post.setShares(dto.getShares());
        post.setLive(dto.isLive());
        post.setCreatedAt(dto.getCreatedAt());
        if (dto.getUpdatedAt() != null) post.setUpdatedAt(dto.getUpdatedAt());
        if (user != null) post.setUser(user);
        return post;
    }

    @PostMapping("/create")
    public ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest postRequest) {
        if (postRequest.title() == null || postRequest.title().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty!");
        }
        if (postRequest.userId() == null){
            throw  new IllegalArgumentException("Author ID cannot be empty!");
        }
        if (postRequest.content() == null){
            throw new IllegalArgumentException("Content cannot be null!");
        }

        // 🔹 Moderation Checks
        ModerationResult titleCheck = moderationService.moderateText(postRequest.title());
        if (titleCheck.isFlagged()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new CreatePostResponse(null, false));
        }

        ModerationResult summaryCheck = moderationService.moderateText(postRequest.summary());
        if (summaryCheck.isFlagged()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new CreatePostResponse(null, false));
        }

        ModerationResult contentCheck = moderationService.moderateText(postRequest.content());
        if (contentCheck.isFlagged()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new CreatePostResponse(null, false));
        }

        Post post = new Post();
        User user = userService.getUserById(postRequest.userId());
        post.setUser(user);
        post.setTitle(postRequest.title());
        post.setContent(postRequest.content());
        post.setSummary(postRequest.summary());
        post.setCreatedAt(LocalDateTime.now());
        post.setImageUrl(postRequest.imageUrl());
        post.setLive(true);
        Post savedPost = postService.createPost(post);

        CreatePostResponse response = new CreatePostResponse(convertToDTO(savedPost), true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<PostsResponse> getAllPosts(){
        List<Post> posts = postService.getAllPosts();
        List<PostDTO> dtos = posts.stream().map(this::convertToDTO).toList();
        PostsResponse response = new PostsResponse(dtos, (long) dtos.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable Long userId) {
        List<Post> posts = postService.getPostsByUserId(userId);
        List<PostDTO> dtos = posts.stream().map(this::convertToDTO).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post == null) {
            throw new IllegalArgumentException("Invalid Post ID. No post found!!");
        }
        return ResponseEntity.ok(convertToDTO(post));
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<PostDTO>> searchByTitle(@RequestParam String keyword) {
        List<Post> posts = postService.getPostByTitleContaining(keyword);
        List<PostDTO> dtos = posts.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search/summary")
    public ResponseEntity<List<PostDTO>> searchBySummary(@RequestParam String keyword) {
        List<Post> posts = postService.getPostBySummaryContaining(keyword);
        List<PostDTO> dtos = posts.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) {
        postService.deletePostById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePost(@RequestBody PostDTO postDTO) {
        Post post = convertToEntity(postDTO);
        postService.deletePost(post);
        return ResponseEntity.noContent().build();
    }
}
