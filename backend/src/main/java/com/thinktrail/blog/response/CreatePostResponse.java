package com.thinktrail.blog.response;

import com.thinktrail.blog.dto.PostDTO;

public record CreatePostResponse(
        PostDTO postDTO,
        Boolean success
) {}
