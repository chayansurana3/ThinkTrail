package com.thinktrail.blog.dto;

import com.thinktrail.blog.model.*;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private Integer age;
    private Integer avatar;
    private String password;
    private String bio;
    private String gender;
    private String role;
    private List<Long> postIds;
    private List<Long> commentIds;
    private List<Long> likeIds;
    private List<Long> followerIds;
    private List<Long> followingIds;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setMiddleName(user.getMiddleName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setGender(user.getGender());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);
        dto.setAge(user.getAge());
        dto.setAvatar(user.getAvatar());

        if (user.getPosts() != null)
            dto.setPostIds(user.getPosts().stream().map(Post::getId).collect(Collectors.toList()));

        if (user.getComments() != null)
            dto.setCommentIds(user.getComments().stream().map(Comment::getId).collect(Collectors.toList()));

        if (user.getLikes() != null)
            dto.setLikeIds(user.getLikes().stream().map(Like::getId).collect(Collectors.toList()));

        if (user.getFollowers() != null)
            dto.setFollowerIds(user.getFollowers().stream().map(User::getId).collect(Collectors.toList()));

        if (user.getFollowing() != null)
            dto.setFollowingIds(user.getFollowing().stream().map(User::getId).collect(Collectors.toList()));

        return dto;
    }

    public static User toEntity(UserDTO dto) {
        User user = User.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .middleName(dto.getMiddleName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .age(dto.getAge() != null ? dto.getAge() : 0)
                .bio(dto.getBio())
                .avatar(dto.getAvatar() != null ? dto.getAvatar() : 0)
                .gender(dto.getGender())
                .role(dto.getRole() != null ? Enum.valueOf(Role.class, dto.getRole()) : Role.USER)
                .build();

        if (dto.getFollowerIds() != null) {
            List<User> followers = dto.getFollowerIds().stream()
                    .map(id -> User.builder().id(id).build())
                    .collect(Collectors.toList());
            user.setFollowers(followers);
        }

        if (dto.getFollowingIds() != null) {
            List<User> following = dto.getFollowingIds().stream()
                    .map(id -> User.builder().id(id).build())
                    .collect(Collectors.toList());
            user.setFollowing(following);
        }

        return user;
    }
}