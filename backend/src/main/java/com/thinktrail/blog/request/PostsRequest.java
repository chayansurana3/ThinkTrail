package com.thinktrail.blog.request;

import java.util.List;

public record PostsRequest(
        List<Long> PostIds
) {}
