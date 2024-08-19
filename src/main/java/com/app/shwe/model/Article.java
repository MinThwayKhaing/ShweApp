package com.app.shwe.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Article extends CommonDTO {

    private String title;

    @Column(length = 500)
    private String description;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
}