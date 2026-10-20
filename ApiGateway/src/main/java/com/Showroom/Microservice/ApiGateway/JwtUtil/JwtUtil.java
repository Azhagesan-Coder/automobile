package com.Showroom.Microservice.ApiGateway.JwtUtil;

import java.util.Date;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	private SecretKey secretkey;
	
	@PostConstruct
	public void init() {
		
		if(secret == null || secret.getBytes(java.nio.charset.StandardCharsets.UTF_8).length < 32) {
			throw new IllegalArgumentException("Set JWT_SECRET to a securely generated secret containing at least 32 UTF-8 bytes; HS256 is the algorithm, not the secret");
		}
		this.secretkey = Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	}
	
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
				.signWith(secretkey, SignatureAlgorithm.HS256)
				.compact();
	}
}
