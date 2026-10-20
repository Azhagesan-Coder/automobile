package com.Showroom.Microservice.ApiGateway;
import com.Showroom.Microservice.ApiGateway.JwtUtil.JwtUtil;
import com.Showroom.Microservice.ApiGateway.SecurityConfiguration.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.oauth2.jwt.JwtException;
import static org.junit.jupiter.api.Assertions.*;
class JwtVerificationTest {
    @Test void expiredTokenIsRejected() {
        String secret = "test-only-secret-with-at-least-32-bytes";
        String token = io.jsonwebtoken.Jwts.builder().setSubject("alice")
            .setExpiration(new java.util.Date(System.currentTimeMillis() - 120_000))
            .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8)),
                io.jsonwebtoken.SignatureAlgorithm.HS256).compact();
        assertThrows(JwtException.class, () -> new SecurityConfiguration().jwtDecoder(secret).decode(token));
    }
    @Test void generatedTokenCanBeVerifiedAndWrongKeyIsRejected() {
        String secret = "test-only-secret-with-at-least-32-bytes";
        JwtUtil util = new JwtUtil();
        ReflectionTestUtils.setField(util, "secret", secret); util.init();
        String token = util.generateToken("alice");
        SecurityConfiguration config = new SecurityConfiguration();
        assertEquals("alice", config.jwtDecoder(secret).decode(token).getSubject());
        assertThrows(JwtException.class, () -> config.jwtDecoder("different-test-key-at-least-32-bytes").decode(token));
    }
}
