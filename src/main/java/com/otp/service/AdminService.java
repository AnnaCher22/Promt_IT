package com.otp.service;

import com.otp.dao.OtpCodeRepository;
import com.otp.dao.OtpConfigRepository;
import com.otp.dao.UserRepository;
import com.otp.entity.OtpCode;
import com.otp.entity.OtpConfig;
import com.otp.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final OtpConfigRepository otpConfigRepository;
    private final UserRepository userRepository;
    private final OtpCodeRepository otpCodeRepository;

    public OtpConfig updateOtpConfig(OtpConfig config) {
        otpConfigRepository.deleteAll(); // гарантируем, что будет только одна запись
        return otpConfigRepository.save(config);
    }

    public List<User> getAllNonAdminUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getRole().equalsIgnoreCase("ADMIN"))
                .toList();
    }

    public void deleteUserAndOtps(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        List<OtpCode> codes = otpCodeRepository.findAll()
                .stream()
                .filter(code -> code.getUser().getId().equals(userId))
                .toList();

        otpCodeRepository.deleteAll(codes);
        userRepository.delete(user);
    }
}