package com.app.shwe.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.app.shwe.model.Process;

public interface ProcessRepository extends JpaRepository<Process, Long> {

	List<Process> findAllByOrderByImageorderAsc();


}
