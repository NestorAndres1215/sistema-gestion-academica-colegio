package com.san_andres.backend.shared.security.jwt;

import com.san_andres.backend.role.domain.model.Role;
import com.san_andres.backend.shared.security.config.JwtProperties;
import com.san_andres.backend.users.domain.model.User;
import com.san_andres.backend.shared.security.port.TokenProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAdapter implements TokenProviderPort {

    private final JwtProperties properties;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
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
                                .toList()
                )
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(properties.getExpirationMs())))
                .signWith(getSigningKey())
                .compact();
    }

    @Override
    public String extractUserId(String token) {
        return extractAllClaims(token)
                .getSubject();
    }

    @Override
    public boolean validateToken(String token, String userId) {
        Claims claims = extractAllClaims(token);
        return userId.equals(claims.getSubject())
                && claims.getExpiration()
                .after(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}