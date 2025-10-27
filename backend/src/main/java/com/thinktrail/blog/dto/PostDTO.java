package com.thinktrail.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String imageUrl;
    private Long likes;
    private Long shares;
    private boolean isLive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long authorId;
    private String authorName;
}