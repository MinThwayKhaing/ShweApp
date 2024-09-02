package com.app.shwe.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tm30DTOResponseDTO {
    private int id;
    private Date createdDate;
    private String syskey;
    private String passportBio;
    private String visaPage;
    private String duration;
    private String contactNumber;
    private String status;
    private String userName;

}
