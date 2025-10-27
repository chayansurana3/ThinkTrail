package com.thinktrail.blog.response;

import com.thinktrail.blog.dto.UserDTO;

public record AuthResponse(
        String token,
        UserDTO userDTO
) {}