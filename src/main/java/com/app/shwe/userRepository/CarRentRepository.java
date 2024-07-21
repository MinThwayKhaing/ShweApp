package com.app.shwe.userRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.CarRent;

@Repository
public interface CarRentRepository extends JpaRepository<CarRent, Integer>{


    @Query("SELECT COUNT(c) FROM CarRent c WHERE c.id = :id")
    int checkCarById(@Param("id") int id);
}
