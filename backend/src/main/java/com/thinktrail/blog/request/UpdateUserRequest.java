package com.thinktrail.blog.request;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        String middleName,
        String bio,
        String gender,
        Integer age,
        Integer avatar,
        Long userId
) {}