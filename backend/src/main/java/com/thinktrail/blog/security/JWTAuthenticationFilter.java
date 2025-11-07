package com.thinktrail.blog.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwt;

    public JWTAuthenticationFilter(JWTService jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String path = req.getServletPath();
        if (path.equals("/") ||
                path.equals("/index.html") ||
                path.startsWith("/api/auth/") ||
                path.startsWith("/actuator/health") ||
                path.startsWith("/static/") ||
                path.startsWith("/assets/") ||
                path.startsWith("/images/") ||
                path.startsWith("/css/") ||
                path.startsWith("/js/") ||
                path.endsWith(".js") ||
                path.endsWith(".css") ||
                path.endsWith(".html") ||
                path.endsWith(".ico") ||
                path.endsWith(".png") ||
                path.endsWith(".jpg") ||
                path.endsWith(".svg") ||
                path.endsWith(".woff") ||
                path.endsWith(".woff2") ||
                path.endsWith(".ttf") ||
                path.equals("/favicon.ico") ||
                path.equals("/manifest.json")) {
            chain.doFilter(req, res);
            return;
        }

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var jws = jwt.parseToken(token);
                Claims c = jws.getBody();
                String email = c.getSubject();
                // If you later add roles into claims, map them here; for now use ROLE_USER
                var auth = new UsernamePasswordAuthenticationToken(email, null,
                        List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception ignored) {
                // invalid/expired token -> proceed unauthenticated
            }
        }
        chain.doFilter(req, res);
    }
}