package com.app.shwe.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;


@Entity
@Data
public class Process extends CommonDTO{
   
    private int imageorder;
    private String name;
    private String description;
    private double price;
    private String imglink;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

//    @OneToMany(mappedBy = "service")
//    private Set<Appointment> appointments;

}
