package com.app.shwe.dto;

import com.app.shwe.model.CarRentLocation;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data

public class CarPriceRequest {

    private double price;
    private int type;
    private int carRentLocationId;
}
