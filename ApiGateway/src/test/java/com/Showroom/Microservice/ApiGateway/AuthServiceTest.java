package com.Showroom.Microservice.ApiGateway;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import com.Showroom.Microservice.ApiGateway.DTO.AuthRequest;
import com.Showroom.Microservice.ApiGateway.JwtUtil.JwtUtil;
import com.Showroom.Microservice.ApiGateway.SecurityModel.User;
import com.Showroom.Microservice.ApiGateway.SecurityRepository.UserRepository;
import com.Showroom.Microservice.ApiGateway.SecurityService.AuthService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock UserRepository repository;
    @Mock PasswordEncoder encoder;
    @Mock JwtUtil jwt;
    @InjectMocks AuthService service;

    private AuthRequest request() {
        AuthRequest request = new AuthRequest();
        request.setUsername("alice"); request.setPassword("password");
        return request;
    }

    @Test void loginUsesRetrievedUser() {
        User user = new User(); user.setUsername("alice"); user.setPassword("encoded");
        when(repository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(encoder.matches("password", "encoded")).thenReturn(true);
        when(jwt.generateToken("alice")).thenReturn("signed-token");
        assertEquals("signed-token", service.login(request()).getToken());
    }

    @Test void wrongPasswordDoesNotIssueToken() {
        User user = new User(); user.setPassword("encoded");
        when(repository.findByUsername("alice")).thenReturn(Optional.of(user));
        ResponseStatusException error = assertThrows(ResponseStatusException.class, () -> service.login(request()));
        assertEquals(401, error.getStatusCode().value());
        verifyNoInteractions(jwt);
    }

    @Test void registrationPersistsEntityWithEncodedPassword() {
        when(repository.findByUsername("alice")).thenReturn(Optional.empty());
        when(encoder.encode("password")).thenReturn("encoded");
        service.register(request());
        ArgumentCaptor<User> captured = ArgumentCaptor.forClass(User.class);
        verify(repository).saveAndFlush(captured.capture());
        assertEquals("alice", captured.getValue().getUsername());
        assertEquals("encoded", captured.getValue().getPassword());
        assertEquals("USER", captured.getValue().getRole());
    }

    @Test void duplicateUsernameIsConflict() {
        when(repository.findByUsername("alice")).thenReturn(Optional.of(new User()));
        assertEquals(409, assertThrows(ResponseStatusException.class, () -> service.register(request())).getStatusCode().value());
        verify(repository, never()).saveAndFlush(any());
    }
}
