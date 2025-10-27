package com.thinktrail.blog.request;

public record LoginRequest(
        String email,
        String password
) {}
