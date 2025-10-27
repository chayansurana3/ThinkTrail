package com.thinktrail.blog.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.thinktrail.blog.dto.*;
import com.thinktrail.blog.exceptions.EmailAlreadyExistsException;
import com.thinktrail.blog.model.*;
import com.thinktrail.blog.repository.UserRepository;
import com.thinktrail.blog.request.LoginRequest;
import com.thinktrail.blog.request.RegisterRequest;
import com.thinktrail.blog.response.AuthResponse;
import com.thinktrail.blog.security.JWTService;
import com.thinktrail.blog.service.AuthAppService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthAppServiceImpl implements AuthAppService {

    private final UserRepository users;
    private final AuthenticationManager authManager;
    private final PasswordEncoder encoder;
    private final JWTService jwt;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest r) {
        if (users.existsByEmail(r.email())) {
            throw new EmailAlreadyExistsException("Email already registered. Please login.");
        }

        var u = User.builder()
                .firstName(r.firstName())
                .middleName(r.middleName())
                .lastName(r.lastName())
                .email(r.email())
                .password(encoder.encode(r.password()))
                .role(Role.USER)
                .build();
        users.save(u);

        String fullName = ((u.getFirstName() == null ? "" : u.getFirstName()) +
                (u.getLastName() == null ? "" : " " + u.getLastName())).trim();

        String token = jwt.generateToken(u.getEmail(),
                java.util.Map.of("name", fullName, "role", u.getRole().name()));

        UserDTO dto = UserDTO.fromEntity(u);

        return new AuthResponse(token, dto);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest req) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));

        var u = users.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String fullName = ((u.getFirstName() == null ? "" : u.getFirstName()) +
                (u.getLastName() == null ? "" : " " + u.getLastName())).trim();

        String token = jwt.generateToken(u.getEmail(),
                java.util.Map.of("name", fullName, "role", u.getRole().name()));

        UserDTO dto = UserDTO.fromEntity(u);

        return new AuthResponse(token, dto);
    }

    @Override
    @Transactional
    public AuthResponse loginWithGoogle(String idTokenString) {
        try {
            log.info("Starting login with google");

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                log.error("Google ID token is invalid or could not be verified: {}", idTokenString);
                throw new RuntimeException("Invalid Google ID token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            User u = users.findByEmail(email).orElse(null);

            if (u == null) {
                u = User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .password(UUID.randomUUID().toString())
                        .role(Role.USER)
                        .build();
                users.save(u);
                log.info("Created new user from Google login: {}", email);
            } else {
                log.info("Existing user logging in with Google: {}", email);
            }

            String fullName = ((u.getFirstName() == null ? "" : u.getFirstName()) +
                    (u.getLastName() == null ? "" : " " + u.getLastName())).trim();

            String token = jwt.generateToken(u.getEmail(),
                    Map.of("name", fullName, "role", u.getRole().name()));

            UserDTO dto = UserDTO.fromEntity(u);

            return new AuthResponse(token, dto);

        } catch (Exception e) {
            log.error("Google login failed", e);  // <-- log full stack trace
            throw new RuntimeException("Google login failed: " + e.getMessage(), e);
        }
    }

}
