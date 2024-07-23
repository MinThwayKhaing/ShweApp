package com.app.shwe.userRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.shwe.model.Translator;

public interface TranslatorRepository extends JpaRepository<Translator, Integer>{

	@Query("SELECT t FROM Translator t WHERE (:searchString IS NULL OR :searchString = '' OR "
			+ "t.name LIKE %:searchString% OR t.language LIKE %:searchString% OR t.specialist LIKE %:searchString%)")
	Page<Translator> searchTranslator(@Param("searchString") String searchString, Pageable pageable);
}
