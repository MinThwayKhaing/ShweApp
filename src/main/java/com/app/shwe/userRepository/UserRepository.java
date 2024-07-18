package com.app.shwe.userRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.model.Report;
import com.app.shwe.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserName(String username);

	boolean existsByPhoneNumber(String phoneNumber);

	boolean existsByUserName(String userName);

	Optional<User> findByPhoneNumber(String phoneNumber);

	Optional<User> findByPhoneNumberAndPassword(String phoneNumber, String password);

	@Query("SELECT new com.app.shwe.dto.UserReportDTO(u.userName, r.content) "
			+ "FROM User u JOIN Report r ON r.user.id = u.id WHERE u.id = :id")
	List<UserReportDTO> findUserContentById(String id);

}
