package com.app.shwe.dto;

import lombok.Data;

@Data
public class TranslatorRatingDTO {
    private int id;
    private String name;
    private double averageRating;

    public TranslatorRatingDTO(int id, String name, double averageRating) {
        this.id = id;
        this.name = name;
        this.averageRating = averageRating;
    }
}