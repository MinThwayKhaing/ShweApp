package com.app.shwe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OtpVerificationRequest {
    private String recipient;
    private String otpCode;
    private String token;
}
