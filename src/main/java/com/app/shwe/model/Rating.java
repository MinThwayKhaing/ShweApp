package com.app.shwe.model;

import java.util.Optional;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating extends CommonDTO {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translator_id")
    private Translator translator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_rent_id")
    private CarRent carRent;

    @Column(nullable = false)
    private double rating;

    @Lob
    private String description;

}
