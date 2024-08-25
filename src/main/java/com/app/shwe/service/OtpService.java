package com.app.shwe.service;

import com.app.shwe.dto.UpdateUserRequest;
import com.app.shwe.model.Otp;
import com.app.shwe.model.User;
import com.app.shwe.repository.OtpRepository;
import com.app.shwe.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Random;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class OtpService {

    @Value("${sms.api.url.send}")
    private String sendOtpUrl;

    @Value("${sms.api.url.verify}")
    private String verifyOtpUrl;

    @Value("${sms.api.key}")
    private String apiKey;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserService userService;
    
    @Autowired 
    private UserRepository userRepository;

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
        refCode = "shwe" + refCode;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/x-www-form-urlencoded"); // Form data

        // Custom message with placeholders
        String customMessage = "Your OTP code is {otp}. It is valid for {validity} minutes. Ref: {refcode}";

        // Prepare the request payload as form data (key-value pairs)
        String payload = String.format("recipient=%s&sender_name=%s&ref_code=%s&digit=%d&validity=%d&custom_message=%s",
                recipient, senderName, refCode, 6, OTP_EXPIRATION_MINUTES, customMessage);

        System.out.println("payload is " + payload);

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<String> response = restTemplate.exchange(sendOtpUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            // Parse the JSON response to extract the `token`
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                String token = jsonNode.path("data").path("token").asText();

                System.out.println("Token is: " + token);
                return token;

            } catch (Exception e) {
                e.printStackTrace();
                return "Failed to parse response: " + e.getMessage();
            }
        } else {
            return "Failed to send OTP. Response Code: " + response.getStatusCode();
        }
    }
    
   

    


    public ResponseEntity<String> verifyOtp(String token, String otpCode, String phoneNumber) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // Prepare the payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("token", token);
        payload.put("otp_code", otpCode);

        // Create the request entity
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            // Send the request
            ResponseEntity<String> response = restTemplate.exchange(verifyOtpUrl, HttpMethod.POST, entity,
                    String.class);

            System.out.println("Full response: " + response.getBody());

            // Handle the response by parsing JSON
            return userService.handleParsedResponseForUserRegister(response, phoneNumber);

        } catch (Exception e) {
            // Log any exception that occurs during the process
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while verifying the OTP.");
        }
    }
    
    //WWTT
    public ResponseEntity<String> verifyOtpForUpdate(String token, String otpCode, String phoneNumber) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> payload = new HashMap<>();
        payload.put("token", token);
        payload.put("otp_code", otpCode);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(verifyOtpUrl, HttpMethod.POST, entity, String.class);

            return userService.handleParsedResponseForUserUpdate(response, phoneNumber);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while verifying the OTP.");
        }
    }

    public String generateOtp(int length) {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    public Date calculateExpiryTime(int expiryMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiryMinutes);
        return calendar.getTime();
    }
}