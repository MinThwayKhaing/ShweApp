package com.app.shwe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.shwe.service.OtpService;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {
    @Autowired
    private OtpService smsOtpService;

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyOtp(@RequestParam String recipient,
            @RequestParam String otpCode) {
        try {
            return smsOtpService.verifyOtp(recipient, otpCode);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String recipient, @RequestParam String senderName,
            @RequestParam String refCode) {
        String result = smsOtpService.sendOtp(recipient, senderName, refCode);
        return ResponseEntity.ok(result);
    }
}
