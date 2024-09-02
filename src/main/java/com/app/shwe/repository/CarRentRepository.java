package com.app.shwe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.CarRentDTO;
import com.app.shwe.dto.CarRentRatingDTO;
import com.app.shwe.model.CarOrder;
import com.app.shwe.model.CarRent;

@Repository
public interface CarRentRepository extends JpaRepository<CarRent, Integer> {

	@Query("SELECT COUNT(c) FROM CarRent c WHERE c.id = :id")
	int checkCarById(@Param("id") int id);

	@Query("SELECT DISTINCT c.carType FROM CarRent  c")
	List<Integer> findAllCarTypes();

	@Query("SELECT DISTINCT c.carName FROM CarRent c")
	List<String> findAllCarNames();

	@Query("SELECT c FROM CarRent c")
	Page<CarRent> showAllCar(Pageable pageable);

	@Query("SELECT new com.app.shwe.dto.CarRentRatingDTO(c.id, c.carName, AVG(r.rating)) " +
			"FROM CarRent c JOIN Rating r ON c.id = r.carRent.id " +
			"WHERE c.id = :carId " +
			"GROUP BY c.id, c.carName")
	Optional<CarRentRatingDTO> findByIdWithRating(int carId);

	@Query("SELECT new com.app.shwe.dto.CarRentRatingDTO(c.id, c.carName, AVG(r.rating)) " +
			"FROM CarRent c JOIN Rating r ON c.id = r.carRent.id " +
			"GROUP BY c.id, c.carName HAVING AVG(r.rating) >= 4.5")
	Page<CarRentRatingDTO> findAllWithHighRating(Pageable pageable);

	@Query("SELECT new com.app.shwe.dto.CarRentDTO(c.id,c.carName,c.ownerName,c.carNo,c.status,c.license,c.driverPhoneNumber,c.driverName,c.carColor"
			+ ",c.carType,c.image)"
			+ " FROM CarRent c "
			+ " WHERE c.deleteStatus = false AND (:searchString IS NULL OR :searchString = '' OR "
			+ "c.carName LIKE %:searchString% OR c.ownerName LIKE %:searchString% OR c.carNo LIKE %:searchString%)")
	Page<CarRent> carSimpleSearch(@Param("searchString") String searchString, Pageable pageable);

	@Query("SELECT new com.app.shwe.dto.CarRentDTO(c.id, c.carName, c.ownerName, c.carNo, c.status, c.license, c.driverPhoneNumber, c.driverName, c.carColor, c.carType, c.image) "
			+
			"FROM CarRent c WHERE c.id = :id AND c.deleteStatus = false")
	Optional<CarRentDTO> getCarById(@Param("id") int id);

}
