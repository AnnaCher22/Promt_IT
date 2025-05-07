package com.otp.dao;


import com.otp.entity.OtpConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpConfigRepository extends JpaRepository<OtpConfig, Long> {
    OtpConfig findTopByOrderByIdAsc();
}