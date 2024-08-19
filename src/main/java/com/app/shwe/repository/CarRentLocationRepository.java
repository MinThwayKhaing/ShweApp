package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.LocationResponse;
import com.app.shwe.model.CarRentLocation;

@Repository
public interface CarRentLocationRepository extends JpaRepository<CarRentLocation, Integer> {

    @Query("SELECT new com.app.shwe.dto.LocationResponse(cl.id, cl.location) FROM CarRentLocation cl GROUP BY cl.location, cl.id")
    List<LocationResponse> findAllUniqueLocation();

}
