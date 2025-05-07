package com.otp.service;




import com.otp.controller.dto.AuthRequest;
import com.otp.controller.dto.AuthResponse;
import com.otp.controller.dto.RegisterRequest;
import com.otp.dao.UserRepository;
import com.otp.entity.User;
import com.otp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (request.getRole().equalsIgnoreCase("ADMIN")) {
            boolean adminExists = userRepository.findAll()
                    .stream()
                    .anyMatch(user -> user.getRole().equalsIgnoreCase("ADMIN"));
            if (adminExists) {
                throw new RuntimeException("Администратор уже существует");
            }
        }

        User user = User.builder()
                .login(request.getLogin())
                .encryptedPassword(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole().toUpperCase())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .telegram(request.getTelegram())
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(Map.of(), user.getLogin());
        return new AuthResponse(token);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );

        User user = userRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String token = jwtService.generateToken(Map.of(), user.getLogin());
        return new AuthResponse(token);
    }
}