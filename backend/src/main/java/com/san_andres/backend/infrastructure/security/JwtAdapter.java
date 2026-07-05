package com.san_andres.backend.infrastructure.security;

import com.san_andres.backend.domain.models.User;
import com.san_andres.backend.domain.port.repositories.TokenProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements TokenProviderPort {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey signingKey;

    @PostConstruct
    private void init() {
        this.signingKey = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String generateToken(User user) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("id", user.getId())
                .claim(
                        "roles",
                        user.getRoles()
                                .stream()
                                .map(role -> role.getName())
                                .toList()
                )
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public boolean validateToken(String token, String userId) {
        return userId.equals(extractUserId(token))
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}