package com.app.shwe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.app.shwe.model.MainOrder;

@Repository
public interface MainOrderRepository extends JpaRepository<MainOrder, Integer> {

    @Query("SELECT m FROM MainOrder m WHERE m.order_id = :orderId AND m.sys_key = :sysKey")
    Optional<MainOrder> findByOrderIdAndSysKey(@Param("orderId") int orderId, @Param("sysKey") String sysKey);

    @Modifying
    @Query("UPDATE MainOrder m SET m.status = :status WHERE m.sys_key = :sysKey")
    int updateOrderStatusToOnProgress(@Param("status") String status, @Param("sysKey") String sysKey);

    @Query(value = "SELECT mo.order_id, mo.id, mo.sys_key, mo.created_date, u.user_name " +
            "FROM main_order mo " +
            "JOIN user u ON mo.user_id = u.id " +
            "WHERE mo.sys_key LIKE %:searchString% or u.user_name LIKE %:searchString% and mo.status =:status", countQuery = "SELECT COUNT(*) FROM main_order mo JOIN user u ON mo.user_id = u.id "
                    +
                    "WHERE mo.sys_key LIKE %:searchString% or u.user_name LIKE %:searchString% and mo.status =:status", nativeQuery = true)
    Page<Object[]> findBySysKeyAndUserName(@Param("searchString") String sysKey,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT mo FROM MainOrder mo WHERE mo.user.id = :userId")
    Page<MainOrder> findByUserId(Pageable pageable, @Param("userId") int userId);
}
