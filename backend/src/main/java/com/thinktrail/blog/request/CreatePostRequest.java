package com.thinktrail.blog.request;

public record CreatePostRequest(
   String title,
   String imageUrl,
   String content,
   String summary,
   Long userId
) {}
