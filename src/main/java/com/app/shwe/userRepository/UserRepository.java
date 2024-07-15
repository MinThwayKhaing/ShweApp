package com.app.shwe.userRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.shwe.model.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{
	

	Optional<User> findByUserName(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByUserName(String userName);
}
