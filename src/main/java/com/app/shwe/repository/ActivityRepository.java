package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
	
	@Query("SELECT a FROM Activity a")
	List<Activity> getAllActivityList();

}