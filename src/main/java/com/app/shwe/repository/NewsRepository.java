package com.app.shwe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer>{
	
	@Query("SELECT n FROM News n ORDER BY n.createdDate DESC")
	Page<News> getAllNewsByDate(Pageable pageable);
	
	@Query("SELECT COUNT(n) FROM News n WHERE n.id = :id")
	int checkNewsById(@Param("id") int id);

}
