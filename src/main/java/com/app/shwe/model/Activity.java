package com.app.shwe.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Activity extends CommonDTO {

    private String name;

    @OneToMany(mappedBy = "activity")
    private List<Article> articles;
}
