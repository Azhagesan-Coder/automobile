package com.Showroom.Microservice.ApiGateway.SecurityService;

import com.Showroom.Microservice.ApiGateway.DTO.*;
import com.Showroom.Microservice.ApiGateway.JwtUtil.JwtUtil;
import com.Showroom.Microservice.ApiGateway.SecurityModel.User;
import com.Showroom.Microservice.ApiGateway.SecurityRepository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AuthService(JwtUtil jwtUtil, UserRepository repository, PasswordEncoder encoder) {
        this.jwtUtil = jwtUtil;
        this.repository = repository;
        this.encoder = encoder;
    }

    public String register(AuthRequest request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole("USER");
        try {
            repository.saveAndFlush(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        return "User registered successfully";
    }

    public AuthResponse login(AuthRequest request) {
        User user = repository.findByUsername(request.getUsername())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return new AuthResponse(jwtUtil.generateToken(user.getUsername()));
    }
}