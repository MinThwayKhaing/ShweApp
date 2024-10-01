package com.app.shwe.repository;

import com.app.shwe.model.NotificationTokenEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationTokenRepository extends JpaRepository<NotificationTokenEntity, Integer> {

    Optional<NotificationTokenEntity> findByUserId(int userId);

    // Method to find the latest notification token by userId
    @Query(value = "SELECT * FROM notification_tokens WHERE user_id = :userId ORDER BY id DESC LIMIT 1", nativeQuery = true)
    Optional<NotificationTokenEntity> findLatestByUserId(int userId);

    // Method to delete all records except the latest one
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM notification_tokens WHERE user_id = :userId AND id != :latestId", nativeQuery = true)
    void deleteAllExceptLatestByUserId(int userId, int latestId);

    Optional<NotificationTokenEntity> findByNotifToken(String notifToken);

    // Add more queries if needed
}
