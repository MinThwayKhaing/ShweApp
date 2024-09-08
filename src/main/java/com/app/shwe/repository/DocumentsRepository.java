package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Documents;
import com.app.shwe.model.MainOrder;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Integer> {

	@Query("SELECT d FROM Documents d WHERE d.order.sys_key = :sysKey")
	List<Documents> findByOrder(@Param("sysKey") String syskey);

}
