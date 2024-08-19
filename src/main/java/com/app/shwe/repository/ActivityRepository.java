package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

}