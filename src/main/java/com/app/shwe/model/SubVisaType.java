package com.app.shwe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubVisaType extends CommonDTO{

    private double price;
    
    private String duration;

    @ManyToOne
    @JoinColumn(name = "visa_id")
    private VisaType visa;
    
}
