package com.example.mealplannerbackend.controller;

import com.example.mealplannerbackend.dto.NotificationDTO;
import com.example.mealplannerbackend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<NotificationDTO> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/user/{id}")
    public List<NotificationDTO> getNotificationsOfUser(@PathVariable Long id) {
        return notificationService.getNotificationsOfUser(id);
    }

    @GetMapping("/{id}")
    public NotificationDTO getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    @PostMapping
    public NotificationDTO createNotification(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.createNotification(notificationDTO);
    }

    @PutMapping("/{id}")
    public NotificationDTO updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO) {
        return notificationService.updateNotification(id, notificationDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
    }
}
