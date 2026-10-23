package com.example.campus.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/** Issues and verifies signed access tokens. The secret comes from configuration. */
@Component
public class JwtUtil {

    private static final int MIN_SECRET_BYTES = 32;

    private final SecretKey signingKey;
    private final Duration ttl;
    private final JwtParser parser;

    public JwtUtil(@Value("${app.jwt.secret}") String secret,
                   @Value("${app.jwt.ttl-minutes:480}") long ttlMinutes) {
        byte[] raw = secret.getBytes(StandardCharsets.UTF_8);
        if (raw.length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                "app.jwt.secret must be at least " + MIN_SECRET_BYTES + " bytes long");
        }
        this.signingKey = Keys.hmacShaKeyFor(raw);
        this.ttl = Duration.ofMinutes(ttlMinutes);
        this.parser = Jwts.parser().verifyWith(signingKey).build();
    }

    public String issue(String subject, String role) {
        Instant now = Instant.now();
        return Jwts.builder()
                   .subject(subject)
                   .claim("role", role)
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(now.plus(ttl)))
                   .signWith(signingKey)
                   .compact();
    }

    public UsernamePasswordAuthenticationToken authenticate(String token) {
        Claims claims = parser.parseSignedClaims(token).getPayload();
        String role = claims.get("role", String.class);
        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }
}
