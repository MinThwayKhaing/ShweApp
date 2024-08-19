package com.app.shwe.dto;

import lombok.Data;

@Data
public class CarRentRatingDTO {
    private int id;
    private String name;
    private double averageRating;

    public CarRentRatingDTO(int id, String name, double averageRating) {
        this.id = id;
        this.name = name;
        this.averageRating = averageRating;
    }

}