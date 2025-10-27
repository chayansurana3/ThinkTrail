package com.thinktrail.blog.request;

public record RegisterRequest(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String password
) {}