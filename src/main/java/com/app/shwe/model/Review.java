package com.app.shwe.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review extends CommonDTO {
    private String description;
    private Integer rating;
    private Long carId;
    private Long translatorId;

}
