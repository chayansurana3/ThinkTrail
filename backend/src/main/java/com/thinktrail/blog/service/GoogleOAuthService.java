package com.thinktrail.blog.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

public interface GoogleOAuthService {
    GoogleIdToken.Payload verifyToken(String token) throws Exception;
}
