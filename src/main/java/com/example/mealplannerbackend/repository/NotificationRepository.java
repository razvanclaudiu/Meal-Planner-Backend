package com.example.mealplannerbackend.repository;

import com.example.mealplannerbackend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> getAllByUserIdAndNotificationShownFalse(Long id);
}
