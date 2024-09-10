package com.app.shwe.repository;

import com.app.shwe.dto.CarOrderResponseAdminDTO;
import com.app.shwe.dto.CarOrderResponseDTO;
import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.utils.OrderStatus;

import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarOrderRepository extends JpaRepository<CarOrder, Integer> {
        @Query("SELECT new com.app.shwe.dto.CarOrderResponseAdminDTO(o.sysKey, o.fromLocation, o.toLocation, o.pickUpDate, o.pickUpTime, o.fromDate, o.toDate, o.carType, o.driver, o.status, o.customerPhoneNumber, o.pickUpLocation, o.carBrand, o.price, o.createdDate,c.id, c.carName, c.ownerName, c.carNo, c.status, c.license, c.image, c.driverName, c.driverPhoneNumber, c.carColor, u.id, u.userName, u.image) "
                        + "FROM CarOrder o "
                        + "LEFT JOIN o.carId c "
                        + "JOIN o.user u "
                        + "WHERE o.sysKey = :sysKey")
        Optional<CarOrderResponseAdminDTO> findCarOrderDetailsBySysKey(@Param("sysKey") String sysKey);

        @Query(value = "SELECT * FROM car_order WHERE id = :id", nativeQuery = true)
        Optional<CarOrder> findCarOrderById(@Param("id") int id);

        @Query(value = "SELECT * FROM car_order WHERE sys_key = :sys_key", nativeQuery = true)
        Optional<CarOrder> findCarOrderBySysKey(@Param("sys_key") String sys_key);

        @Query("SELECT new com.app.shwe.dto.CarOrderResponseDTO(o.sysKey,c.id,o.createdDate,o.carType,o.carBrand,c.driverName,c.image,o.status)"
                        + " FROM CarRent c JOIN CarOrder o ON c.id = o.carId.id WHERE"
                        + " (LOWER(o.carBrand) LIKE LOWER(CONCAT('%', :searchString, '%'))"
                        + " OR LOWER(c.driverName) LIKE LOWER(CONCAT('%', :searchString, '%')))")
        Page<CarOrderResponseDTO> showCarOrder(@Param("searchString") String searchString, Pageable pageable);

        @Query("SELECT new com.app.shwe.dto.CarOrderResponseDTO(o.sysKey,c.id,o.createdDate,o.carType,o.carBrand,c.driverName,c.image,o.status)"
                        + " FROM CarRent c JOIN CarOrder o ON c.id = o.carId.id WHERE"
                        + " (LOWER(o.carBrand) LIKE LOWER(CONCAT('%', :searchString, '%'))"
                        + " OR LOWER(c.driverName) LIKE LOWER(CONCAT('%', :searchString, '%')))"
                        + " AND o.createdBy = :userId")
        Page<CarOrderResponseDTO> findOrderByUserId(@Param("userId") int userId,
                        @Param("searchString") String searchString,
                        Pageable pageable);

        @Modifying
        @Query("UPDATE CarOrder c SET c.status = :status WHERE c.id = :id")
        void updateOrder(@Param("id") int id, @Param("status") String status);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("SELECT c FROM CarOrder c WHERE c.id = :id")
        CarOrder findByIdForUpdate(@Param("id") int id);

        @Query("SELECT COALESCE(MAX(t.sysKey), 'CR00000000') FROM CarOrder t")
        String findMaxSysKey();
}
