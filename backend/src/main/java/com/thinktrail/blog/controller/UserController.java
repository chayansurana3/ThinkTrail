package com.thinktrail.blog.controller;

import com.thinktrail.blog.dto.UserDTO;
import com.thinktrail.blog.model.*;
import com.thinktrail.blog.repository.UserRepository;
import com.thinktrail.blog.request.UpdateUserRequest;
import com.thinktrail.blog.response.UpdateUserResponse;
import com.thinktrail.blog.service.UserService;
import com.thinktrail.blog.service.impl.ModerationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ModerationServiceImpl moderationService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        if (userDTO.getBio() != null && !userDTO.getBio().isBlank()) {
            ModerationResult bioCheck = moderationService.moderateText(userDTO.getBio());
            if (bioCheck.isFlagged()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
        }

        User user = UserDTO.toEntity(userDTO);
        User created = userService.createUser(user);
        return ResponseEntity.ok(UserDTO.fromEntity(created));
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        Optional<User> userOpt = userRepository.findById(updateUserRequest.userId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();

        if (updateUserRequest.age() >= 1 && updateUserRequest.age() <= 150)
            user.setAge(updateUserRequest.age());

        if (updateUserRequest.firstName() != null)
            user.setFirstName(updateUserRequest.firstName());

        if (updateUserRequest.middleName() != null)
            user.setMiddleName(updateUserRequest.middleName());

        if (updateUserRequest.lastName() != null)
            user.setLastName(updateUserRequest.lastName());

        if (updateUserRequest.bio() != null) {
            ModerationResult bioCheck = moderationService.moderateText(updateUserRequest.bio());
            if (bioCheck.isFlagged()) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }
            user.setBio(updateUserRequest.bio());
        }

        if (updateUserRequest.gender() != null)
            user.setGender(updateUserRequest.gender());

        if (updateUserRequest.avatar() != null)
            user.setAvatar(updateUserRequest.avatar());

        User updatedUser = userRepository.save(user);

        UpdateUserResponse response = new UpdateUserResponse(UserDTO.fromEntity(updatedUser), true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/follow/{targetId}")
    public ResponseEntity<UserDTO> followUser(@PathVariable Long id, @PathVariable Long targetId) {
        User updated = userService.followUser(id, targetId);
        return ResponseEntity.ok(UserDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}/unfollow/{targetId}")
    public ResponseEntity<UserDTO> unfollowUser(@PathVariable Long id, @PathVariable Long targetId) {
        User updated = userService.unfollowUser(id, targetId);
        return ResponseEntity.ok(UserDTO.fromEntity(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("Invalid User ID. User not found!!!");
        }
        return ResponseEntity.ok(UserDTO.fromEntity(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Long>> getAllPostsByUser(@PathVariable Long id) {
        List<Long> postIds = userService.getAllPostsByUser(id).stream()
                .map(Post::getId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postIds);
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<Long>> getAllLikesByUser(@PathVariable Long id) {
        List<Long> likeIds = userService.getAllLikesByUser(id).stream()
                .map(Like::getId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(likeIds);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<Long>> getAllCommentsByUser(@PathVariable Long id) {
        List<Long> commentIds = userService.getAllCommentsByUser(id).stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentIds);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody UserDTO userDTO) {
        userService.deleteUser(UserDTO.toEntity(userDTO));
        return ResponseEntity.noContent().build();
    }
}