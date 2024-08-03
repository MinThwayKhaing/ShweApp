package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.shwe.dto.TranslatorOrderResponseDTO;
import com.app.shwe.model.TranslatorOrder;

import jakarta.transaction.Transactional;

public interface TranslatorOrderRepostitory extends JpaRepository<TranslatorOrder, Integer>{
	
	@Query("SELECT new com.app.shwe.dto.TranslatorOrderResponseDTO(t.id,o.createdDate,t.name,t.specialist,o.status,t.image,t.chatLink)"
			+ " FROM Translator t JOIN TranslatorOrder o ON t.id=o.translator.id WHERE o.createdBy = :id")
	List<TranslatorOrderResponseDTO> getHireTranslator(int id);
	
	@Modifying
    @Transactional
    @Query("UPDATE TranslatorOrder t SET t.status = :status WHERE t.id = :id")
    void cancelOrder(@Param("id") int id, @Param("status") String status);
	
	 @Query("SELECT new com.app.shwe.dto.TranslatorOrderResponseDTO(t.id,o.createdDate,t.name,t.specialist,o.status,t.image,t.chatLink) "
	            + "FROM Translator t JOIN TranslatorOrder o ON t.id = o.translator.id "
	            + "WHERE "
	            + "(LOWER(t.name) LIKE LOWER(CONCAT('%', :searchString, '%')) "
	            + "OR LOWER(t.specialist) LIKE LOWER(CONCAT('%', :searchString, '%')))")
	    Page<TranslatorOrderResponseDTO> searchHireTranslator(@Param("searchString") String searchString, Pageable pageable);

}
