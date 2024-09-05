package com.app.shwe.dto;

import java.util.Date;
import java.util.TimeZone;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class TranslatorOrderDetailResponseDTO {
    private String sysKey;
    private String status;
    private String fromDate;
    private String toDate;
    private String meetingDate;
    private String meetingPoint;
    private String phoneNumber;
    private String usedFor;
    private String meetingTime; // assuming it's stored as String

    // Translator-related fields
    private String translatorName;
    private String translatorImage;
    private String translatorChatLink;
    private String createdByUsername;

    // Constructor matching the JPQL query
    public TranslatorOrderDetailResponseDTO(
            String sysKey, String status, String fromDate, String toDate, String meetingDate,
            String meetingPoint, String phoneNumber, String usedFor, String meetingTime,
            String translatorName, String translatorImage, String translatorChatLink,
            String createdByUsername) {
        this.sysKey = sysKey;
        this.status = status;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.meetingDate = meetingDate;
        this.meetingPoint = meetingPoint;
        this.phoneNumber = phoneNumber;
        this.usedFor = usedFor;
        this.meetingTime = meetingTime;
        this.translatorName = translatorName;
        this.translatorImage = translatorImage;
        this.translatorChatLink = translatorChatLink;
        this.createdByUsername = createdByUsername;
    }
}
