
package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Otp;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByRecipientAndOtpCode(String recipient, String otpCode);

    Optional<Otp> findByRefCodeAndOtpCode(String refCode, String otpCode);
}
