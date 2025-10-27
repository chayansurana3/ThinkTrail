package com.thinktrail.blog.controller;

import com.thinktrail.blog.model.User;
import com.thinktrail.blog.request.GoogleRequest;
import com.thinktrail.blog.request.RegisterRequest;
import com.thinktrail.blog.request.LoginRequest;
import com.thinktrail.blog.response.AuthResponse;
import com.thinktrail.blog.service.impl.AuthAppServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthAppServiceImpl authAppServiceImpl;

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody RegisterRequest dto) {
        return authAppServiceImpl.register(dto);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest dto) {
        return authAppServiceImpl.login(dto);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok((User) authentication.getPrincipal());
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> loginWithGoogle(@RequestBody GoogleRequest googleRequest) {
        return ResponseEntity.ok(authAppServiceImpl.loginWithGoogle(googleRequest.getToken()));
    }
}