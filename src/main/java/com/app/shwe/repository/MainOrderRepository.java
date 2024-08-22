package com.app.shwe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.MainOrder;
import com.app.shwe.utils.OrderStatus;

import jakarta.transaction.Transactional;

@Repository
public interface MainOrderRepository extends JpaRepository<MainOrder, Integer> {

    @Query("SELECT m FROM MainOrder m WHERE m.order_id = :orderId AND m.sys_key = :sysKey")
    Optional<MainOrder> findByOrderIdAndSysKey(@Param("orderId") int orderId, @Param("sysKey") String sysKey);

    @Modifying
    @Query("UPDATE MainOrder m SET m.status = :status WHERE m.sys_key = :sysKey")
    int updateOrderStatusToOnProgress(@Param("status") String status, @Param("sysKey") String sysKey);
}
