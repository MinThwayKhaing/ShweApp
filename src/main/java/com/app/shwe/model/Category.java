package com.app.shwe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;


@Entity
@Data
public class Category extends CommonDTO{

    private String name;
    private String label;
   
    @OneToMany(mappedBy = "category")
    private Set<Process> services;

}
