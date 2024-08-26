package com.app.shwe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.SamplePhoto;

@Repository
public interface SamplePhotoRepository extends JpaRepository<SamplePhoto, Integer> {

	@Query("SELECT COUNT(s) FROM SamplePhoto s WHERE s.id = :id")
	int checkExistOrNot(@Param("id") int id);

	@Query("SELECT sp FROM SamplePhoto sp")
	List<SamplePhoto> findCustomSamplePhotos();

}
