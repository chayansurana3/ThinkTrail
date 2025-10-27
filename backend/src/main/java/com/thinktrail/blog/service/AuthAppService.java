package com.thinktrail.blog.service;

import com.thinktrail.blog.response.AuthResponse;
import com.thinktrail.blog.request.LoginRequest;
import com.thinktrail.blog.request.RegisterRequest;

public interface AuthAppService {
    AuthResponse register(RegisterRequest r);
    AuthResponse login(LoginRequest req);
    AuthResponse loginWithGoogle(String idTokenString);
}
