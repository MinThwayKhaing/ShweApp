package com.app.shwe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.Documents;

@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Integer>{

}
