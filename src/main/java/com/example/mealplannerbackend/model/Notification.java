package com.example.mealplannerbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean notificationShown;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long awardId;

    public Notification() {
    }

    public Notification(boolean notificationShown, Long userId, Long awardId) {
        this.notificationShown = notificationShown;
        this.userId = userId;
        this.awardId = awardId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNotificationShown() {
        return notificationShown;
    }

    public void setNotificationShown(boolean notificationShown) {
        this.notificationShown = notificationShown;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAwardId() {
        return awardId;
    }

    public void setAwardId(Long awardId) {
        this.awardId = awardId;
    }
}
