package com.app.shwe.service;

import com.app.shwe.model.NotificationTokenEntity;
import com.app.shwe.model.Role;
import com.app.shwe.model.User;
import com.app.shwe.repository.NotificationTokenRepository;
import com.app.shwe.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirebaseMessagingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationTokenRepository notificationTokenRepository;

    public void sendNotificationToAdmins(String title, String body) {
        // Fetch all users with the role ADMIN
        List<User> adminUsers = userRepository.findAllByRole(Role.ADMIN);
        System.out.println("Admin users retrieved : {}" + adminUsers);
        for (User admin : adminUsers) {
            try {
                // Fetch the notification token for each admin user
                Optional<NotificationTokenEntity> tokenEntity = notificationTokenRepository
                        .findLatestByUserId(admin.getId().intValue());

                if (tokenEntity.isPresent()) {
                    String token = tokenEntity.get().getNotifToken();
                    notificationTokenRepository.deleteAllExceptLatestByUserId(admin.getId().intValue(),
                            tokenEntity.get().getId());
                    sendNotification(title, body, token);

                } else {
                    System.out.println("No notification token found for admin user ID: {}" + admin.getId());
                }
            } catch (Exception e) {
                System.out.println("Error sending notification to admin user ID: {}. Error: {}" + admin.getId() +
                        e.getMessage());
            }
        }
    }

    public void sendNotification(String title, String body, String token) {
        try {
            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(token)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
