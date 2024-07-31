package com.app.shwe.repository;

import com.app.shwe.dto.CarOrderResponseDTO;
import com.app.shwe.model.CarOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarOrderRepository extends JpaRepository<CarOrder, Integer> {

    // @Query("SELECT new com.app.shwe.dto.CarOrderResponseDTO(" +
    //         "u.id, u.userName, co.fromLocation, co.toLocation, " +
    //         "co.pickUpDate, co.pickUpTime, co.fromDate, co.toDate, " +
    //         "co.carType, co.driver, co.orderConfirm, co.customerPhoneNumber, " +
    //         "co.carBrand, co.carId) " +
    //         "FROM CarOrder co " +
    //         "JOIN User u ON co.createdBy = u.id " +
    //         "WHERE u.userName LIKE %:searchString% OR co.fromLocation LIKE %:searchString%")
    // Page<CarOrderResponseDTO> findAllWithSearch(@Param("searchString") String searchString, Pageable pageable);
}
