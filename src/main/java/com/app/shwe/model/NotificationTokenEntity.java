package com.app.shwe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notification_tokens")
public class NotificationTokenEntity extends CommonDTO implements Serializable {

    @Column(name = "user_id", nullable = false)
    private int userId; // Link to the User entity or user ID

    @Column(name = "notif_token", nullable = false, unique = true)
    private String notifToken; // Firebase notification token
}
