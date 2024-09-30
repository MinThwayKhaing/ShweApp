package com.app.shwe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Repository;

import com.app.shwe.dto.ChangePasswordDTO;
import com.app.shwe.dto.UserReportDTO;
import com.app.shwe.model.Report;
import com.app.shwe.model.Role;
import com.app.shwe.model.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findAllByRole(Role role);

	Optional<User> findByUserName(String username);

	@Query("SELECT id "
			+ "FROM User WHERE userName = :username")
	int authUser(String username);

	boolean existsByPhoneNumber(String phoneNumber);

	// boolean existsByPassword(String password);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = :id AND u.password = :password")
	boolean existsByIdAndPassword(@Param("id") Long id, @Param("password") String password);

	boolean existsByUserName(String userName);

	User findByPhoneNumber(String phoneNumber);

	Optional<User> getPhoneNumberById(int id);

	Optional<User> getUserById(int id);

	Optional<User> findByPhoneNumberAndPassword(String phoneNumber, String password);

	@Query("SELECT image "
			+ "FROM User u  WHERE u.id = :id")
	String selectImage(@Param("id") int id);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.image = :img WHERE u.id = :id")
	void imageUpdate(@Param("id") int id, @Param("img") String image);

	@Query("SELECT new com.app.shwe.dto.UserReportDTO(u.userName, r.content) "
			+ "FROM User u JOIN Report r ON r.user.id = u.id WHERE u.id = :id")
	List<UserReportDTO> findUserContentById(String id);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
	void changePassword(@Param("id") int id, @Param("password") String password);

	Optional<User> getUserByPhoneNumber(String phoneNumber);

}
