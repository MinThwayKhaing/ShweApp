package com.app.shwe.dto;

import java.util.Date;
import java.util.TimeZone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslatorOrderRequestDTO {

    private Date fromDate;
    private Date toDate;
    private String usedFor;
    private String meetingPoint;

    private Date meetingDate;
    private TimeZone meetingTime;
    private String phoneNumber;
    private int translator_id;
    private String status;
}
