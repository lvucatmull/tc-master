package com.tcmaster.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tcmaster.dto.AuthDtos;
import com.tcmaster.entity.User;
import com.tcmaster.repository.UserRepository;
import com.tcmaster.security.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, jwtService);
    }

    @Test
    void register_returnsJwtToken() {
        AuthDtos.RegisterRequest request = new AuthDtos.RegisterRequest("test@example.com", "plain", "tester");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken("test@example.com")).thenReturn("jwt-token");

        AuthDtos.AuthResponse response = authService.register(request);

        assertEquals("jwt-token", response.accessToken());
    }

    @Test
    void login_throwsWhenPasswordMismatch() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encoded");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> authService.login(new AuthDtos.LoginRequest("test@example.com", "wrong")));
    }
}
