package com.app.shwe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.CarOrder;

@Repository
public interface CarOrderRepository extends JpaRepository<CarOrder, Long> {

    // @Query("SELECT co FROM CarOrder co WHERE (:searchString IS NULL OR
    // :searchString = '' OR " +
    // "co.car_id LIKE %:searchString% OR co.createdBy LIKE %:searchString%)")
    // Page<CarOrder> findAllWithSearch(@Param("searchString") String searchString,
    // Pageable pageable);
}
