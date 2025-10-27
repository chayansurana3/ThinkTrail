package com.thinktrail.blog.response;

import com.thinktrail.blog.dto.UserDTO;

public record UpdateUserResponse(
        UserDTO userDTO,
        boolean success
) {}
