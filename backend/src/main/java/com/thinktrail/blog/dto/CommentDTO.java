package com.thinktrail.blog.dto;

import com.thinktrail.blog.model.Comment;
import com.thinktrail.blog.model.User;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private Long postId;
    private Long likes;
    private Integer avatar;
    private String authorName;

    public static CommentDTO fromEntity(Comment comment) {
        CommentDTO dto = new CommentDTO();
        User user = comment.getUser();
        String authorName = user.getFirstName() + " " + (!Objects.equals(user.getMiddleName(),null) ? user.getMiddleName() + " " : "") + user.getLastName() + " (" + user.getEmail() + ")";
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUserId(comment.getUser().getId());
        dto.setPostId(comment.getPost().getId());
        dto.setAuthorName(authorName);
        dto.setAvatar(comment.getUser().getAvatar());
        return dto;
    }
}