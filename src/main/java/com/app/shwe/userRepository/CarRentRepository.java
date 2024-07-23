package com.app.shwe.userRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.CarRent;

@Repository
public interface CarRentRepository extends JpaRepository<CarRent, Integer> {

	@Query("SELECT COUNT(c) FROM CarRent c WHERE c.id = :id")
	int checkCarById(@Param("id") int id);

	@Query("SELECT c FROM CarRent c")
	Page<CarRent> showAllCar(Pageable pageable);

	@Query("SELECT c FROM CarRent c WHERE (:searchString IS NULL OR :searchString = '' OR "
			+ "c.carName LIKE %:searchString% OR c.ownerName LIKE %:searchString% OR c.carNo LIKE %:searchString%)")
	Page<CarRent> carSimpleSearch(@Param("searchString") String searchString, Pageable pageable);
}
