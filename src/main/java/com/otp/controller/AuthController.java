package com.otp.controller;

import com.otp.controller.dto.AuthRequest;
import com.otp.controller.dto.AuthResponse;
import com.otp.controller.dto.RegisterRequest;
import com.otp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        log.info("Запрос на регистрацию: email={}, username={}", request.getEmail(), request.getLogin());
        AuthResponse response = authService.register(request);
        log.info("Регистрация прошла успешно: {}", response);
        return response;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        log.info("Запрос на вход: login={}", request.getLogin());
        AuthResponse response = authService.authenticate(request);
        log.info("Вход выполнен успешно: {}", response);
        return response;
    }
}