package com.thinktrail.blog.security;

import com.thinktrail.blog.model.Role;
import com.thinktrail.blog.model.User;
import com.thinktrail.blog.repository.UserRepository;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository users;
    private final JWTService jwt;

    @Value("${app.oauth2-redirect-success}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = (String) principal.getAttributes().get("email");
        String givenName = (String) principal.getAttributes().getOrDefault("given_name", "");
        String familyName = (String) principal.getAttributes().getOrDefault("family_name", "");

        // Provision user if first time
        User user = users.findByEmail(email).orElseGet(() -> {
            User nu = User.builder()
                    .email(email)
                    .firstName(givenName)
                    .lastName(familyName)
                    .role(Role.USER)
                    .build();
            return users.save(nu);
        });

        String fullName = ((user.getFirstName() == null ? "" : user.getFirstName()) +
                (user.getLastName() == null ? "" : " " + user.getLastName())).trim();

        String token = jwt.generateToken(user.getEmail(), Map.of("name", fullName));

        // Send back to Angular with token in URL fragment
        response.sendRedirect(redirectUrl + "#token=" + token);
    }
}
