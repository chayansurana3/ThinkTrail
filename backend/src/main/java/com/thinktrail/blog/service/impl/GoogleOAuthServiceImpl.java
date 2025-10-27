package com.thinktrail.blog.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.thinktrail.blog.service.GoogleOAuthService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleOAuthServiceImpl implements GoogleOAuthService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleOAuthServiceImpl() throws Exception {
        verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList("${spring.security.oauth2.client.registration.google.client-id}")) // replace
                .build();
    }

    @Override
    public GoogleIdToken.Payload verifyToken(String token) throws Exception {
        GoogleIdToken idToken = verifier.verify(token);
        if (idToken != null) {
            return idToken.getPayload();
        }
        return null;
    }
}