package com.app.shwe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.shwe.model.Translator;

public interface TranslatorRepository extends JpaRepository<Translator, Integer> {

	@Query("SELECT t FROM Translator t WHERE (:searchString IS NULL OR :searchString = '' OR "
			+ "t.name LIKE %:searchString% OR t.language LIKE %:searchString% OR t.specialist LIKE %:searchString%)")
	Page<Translator> searchTranslator(@Param("searchString") String searchString, Pageable pageable);
	
	@Query("SELECT COUNT(t) FROM Translator t WHERE t.id = :id")
	int checkTranslator(@Param("id") int id);
}
