package com.example.mealplannerbackend.service;

import com.example.mealplannerbackend.dto.NotificationDTO;
import com.example.mealplannerbackend.exceptions.NotificationNotFoundException;
import com.example.mealplannerbackend.model.Notification;
import com.example.mealplannerbackend.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsOfUser(Long id) {
        List<Notification> notifications = notificationRepository.getAllByUserIdAndNotificationShownFalse(id);
        return notifications.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification not found with id: " + id));
        return convertToDto(notification);
    }

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDto(savedNotification);
    }

    public NotificationDTO updateNotification(Long id, NotificationDTO updatedNotificationDTO) {
        Notification existingNotification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Notification with id " + id + " not found"));

        existingNotification.setNotificationShown(updatedNotificationDTO.isNotificationShown());
        existingNotification.setUserId(updatedNotificationDTO.getUserId());
        existingNotification.setAwardId(updatedNotificationDTO.getAwardId());

        Notification savedNotification = notificationRepository.save(existingNotification);
        return convertToDto(savedNotification);
    }

    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    private NotificationDTO convertToDto(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setNotificationShown(notification.isNotificationShown());
        notificationDTO.setUserId(notification.getUserId());
        notificationDTO.setAwardId(notification.getAwardId());
        return notificationDTO;
    }

    private Notification convertToEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setId(notificationDTO.getId());
        notification.setNotificationShown(notificationDTO.isNotificationShown());
        notification.setUserId(notificationDTO.getUserId());
        notification.setAwardId(notificationDTO.getAwardId());
        return notification;
    }


}
