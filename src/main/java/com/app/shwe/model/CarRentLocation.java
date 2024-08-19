package com.app.shwe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRentLocation extends CommonDTO {
    private String location;

    @OneToMany(mappedBy = "carRentLocation")
    private List<CarPrice> carPrices;
}
