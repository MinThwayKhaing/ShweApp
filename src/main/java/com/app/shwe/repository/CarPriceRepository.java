package com.app.shwe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.TypeResponse;
import com.app.shwe.model.CarPrice;

@Repository
public interface CarPriceRepository extends JpaRepository<CarPrice, Integer> {
    @Query("SELECT cp FROM CarPrice cp")
    List<CarPrice> findAllWithPagination();

    @Query("SELECT new com.app.shwe.dto.TypeResponse(cp.id, cp.type) FROM CarPrice cp GROUP BY cp.type, cp.id")
    List<TypeResponse> findAllUniqueTypes();

    @Query("SELECT cp.price FROM CarPrice cp WHERE LOWER(cp.carRentLocation.location) = LOWER(:location) AND cp.type = :type")
    Optional<Double> findPriceByLocationAndType(@Param("location") String location, @Param("type") int type);

}
