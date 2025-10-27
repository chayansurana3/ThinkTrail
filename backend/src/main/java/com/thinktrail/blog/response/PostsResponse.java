package com.thinktrail.blog.response;

import com.thinktrail.blog.dto.PostDTO;

import java.util.List;

public record PostsResponse(
        List<PostDTO> posts,
        Long size
) {}
