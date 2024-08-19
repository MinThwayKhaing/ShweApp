package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.PendingRegistration;

import java.util.Optional;

@Repository
public interface PendingRegistrationRepository extends JpaRepository<PendingRegistration, Long> {

    Optional<PendingRegistration> findByPhoneNumber(String phoneNumber);

    Optional<PendingRegistration> findByPhoneNumberAndOtp(String phoneNumber, String otp);

    void deleteByPhoneNumber(String phoneNumber);
}
