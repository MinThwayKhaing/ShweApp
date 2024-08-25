package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Report90Day;
import com.app.shwe.model.Report90DayVisaType;
import com.app.shwe.model.TM30Period;

@Repository
public interface Report90DayVisaTypeRepository extends JpaRepository<Report90DayVisaType, Integer> {
    @Query(value = "SELECT * FROM report90_day_visa_type WHERE id = :id", nativeQuery = true)
    Report90DayVisaType findReport90DayVisaTypeById(@Param("id") int id);

    @Query(value = "SELECT * FROM report90_day_visa_type", nativeQuery = true)
    List<Report90DayVisaType> findAllReport90DayVisaTypes();
}
