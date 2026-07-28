package nomad.example.nomad_backend.controller;


import lombok.RequiredArgsConstructor;

import nomad.example.nomad_backend.service.NotificationSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.entity.Notification;
import nomad.example.nomad_backend.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    // İstifadəçinin bütün bildirişləri
    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications() {

        return ResponseEntity.ok(
                notificationService.getMyNotifications()
        );
    }


    // Bildirişi oxundu kimi işarələmək
    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id
    ) {

        notificationService.markAsRead(id);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Notification marked as read"
                )
        );
    }


    // Oxunmamış bildirişlərin sayı (Header-də 🔔 üçün)
    @GetMapping("/unread-count")
    public ResponseEntity<?> getUnreadCount() {

        return ResponseEntity.ok(
                notificationService.getUnreadCount()
        );
    }
}