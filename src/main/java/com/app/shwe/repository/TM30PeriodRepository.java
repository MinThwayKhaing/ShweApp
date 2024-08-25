package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.TM30PeriodDTO;
import com.app.shwe.model.TM30Period;

@Repository
public interface TM30PeriodRepository extends JpaRepository<TM30Period, Integer> {

    @Query("SELECT new com.app.shwe.dto.TM30PeriodDTO(p.id, p.description) FROM TM30Period p")
    List<TM30PeriodDTO> findAllTM30Periods();

    @Query("SELECT new com.app.shwe.dto.TM30PeriodDTO(p.id, p.description) FROM TM30Period p WHERE p.id = :id")
    TM30PeriodDTO findTM30PeriodById(@Param("id") int id);
}
