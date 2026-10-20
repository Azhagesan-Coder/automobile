package com.Showroom.Microservice.ApiGateway.SecurityController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Showroom.Microservice.ApiGateway.DTO.AuthRequest;
import com.Showroom.Microservice.ApiGateway.DTO.AuthResponse;
import com.Showroom.Microservice.ApiGateway.SecurityService.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public String register(@jakarta.validation.Valid @RequestBody AuthRequest request) {
		return authService.register(request);
	}
	
	@PostMapping("/login")
	public AuthResponse login(@jakarta.validation.Valid @RequestBody AuthRequest request) {
		return authService.login(request);
	}
}
