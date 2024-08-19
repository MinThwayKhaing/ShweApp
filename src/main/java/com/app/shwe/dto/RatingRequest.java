package com.app.shwe.dto;

import lombok.Data;

@Data
public class RatingRequest {

    private int translatorId;
    private int carRentId;
    private double rating;
    private String description;
}
