package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.entity.Notification;
import nomad.example.nomad_backend.entity.Opportunity;

import java.util.List;

public interface NotificationService {

    void notifyInterestedUsers(Opportunity opportunity);

    List<Notification> getMyNotifications();

    void markAsRead(Long notificationId);

    Integer getUnreadCount();

}
