package com.thinktrail.blog.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JWTService {
    private final Key key;
    private final long expirationMinutes;

    public JWTService(@Value("${jwt.secret}") String base64Secret,
                      @Value("${jwt.expiration-minutes:60}") long expirationMinutes) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(String email, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(email)
                .addClaims(extraClaims == null ? Map.of() : extraClaims)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirationMinutes * 60)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}