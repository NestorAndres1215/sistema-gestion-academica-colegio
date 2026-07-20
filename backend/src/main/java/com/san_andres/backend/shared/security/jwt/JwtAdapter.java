package com.san_andres.backend.shared.security.jwt;

import com.san_andres.backend.role.domain.model.Role;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.shared.security.port.TokenProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import jakarta.annotation.PostConstruct;

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
        signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateToken(User user) {

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("id", user.getId())
                .claim(
                        "roles",
                        user.getRoles()
                                .stream()
                                .map(Role::getName)
                                .toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String extractUserId(String token) {
        return extractAllClaims(token)
                .getSubject();
    }

    @Override
    public boolean validateToken(String token, String userId) {
        return userId.equals(extractUserId(token))
                && !extractAllClaims(token)
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