package com.app.shwe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.app.shwe.model.Otp;
import com.app.shwe.repository.OtpRepository;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.Calendar;

@Service
public class OtpService {

    // @Value("${sms.api.url.send}")
    // private String sendOtpUrl;
    private String sendOtpUrl;
    // @Value("${sms.api.url.verify}")
    // private String verifyOtpUrl;
    private String verifyOtpUrl;
    // @Value("${sms.api.key}")
    // private String apiKey;
    private String apiKey;
    @Autowired
    private OtpRepository otpRepository;

    private static final int OTP_EXPIRATION_MINUTES = 3;

    public String sendOtp(String recipient, String senderName, String refCode) {
        String otpCode = generateOtp(6);
        Date expiryTime = calculateExpiryTime(OTP_EXPIRATION_MINUTES);

        Otp otp = new Otp();
        otp.setRecipient(recipient);
        otp.setOtpCode(otpCode);
        otp.setRefCode(refCode);
        otp.setExpiryTime(expiryTime);

        otpRepository.save(otp);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String customMessage = "Your OTP code is {otp}. It is valid for {validity} minutes. Ref: {refcode}";
        String payload = String.format("{"
                + "\"recipient\": \"%s\","
                + "\"sender_name\": \"%s\","
                + "\"ref_code\": \"%s\","
                + "\"digit\": %d,"
                + "\"validity\": %d,"
                + "\"custom_message\": \"%s\""
                + "}", recipient, senderName, refCode, 6, OTP_EXPIRATION_MINUTES,
                customMessage.replace("{otp}", otpCode).replace("{validity}", String.valueOf(OTP_EXPIRATION_MINUTES))
                        .replace("{refcode}", refCode));

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return "OTP sent successfully.";
        } else {
            return "Failed to send OTP. Response Code: " + response.getStatusCode();
        }
    }

    public ResponseEntity<Void> verifyOtp(String recipient, String otpCode) {
        Optional<Otp> otpOptional = otpRepository.findByRecipientAndOtpCode(recipient, otpCode);

        if (otpOptional.isPresent()) {
            Otp otp = otpOptional.get();
            if (new Date().before(otp.getExpiryTime())) {
                return ResponseEntity.ok().build(); // HTTP 200 OK
            } else {
                return ResponseEntity.status(HttpStatus.GONE).build(); // HTTP 410 Gone
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // HTTP 401 Unauthorized
        }
    }

    private String generateOtp(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    private Date calculateExpiryTime(int expiryMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiryMinutes);
        return calendar.getTime();
    }
}
