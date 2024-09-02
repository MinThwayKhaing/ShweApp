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
public class Tm30DetailsDTO {
    private int id;
    private String syskey;
    private String passportBio;
    private String visaPage;
    private String duration;
    private String contactNumber;
    private String status;
    private Date createdDate;
    private String userName;
    private String priceDescription;
    private String priceType;
    private double price;
    private String periodDescription;
}
