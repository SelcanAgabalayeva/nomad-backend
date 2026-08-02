package nomad.example.nomad_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    // Bildiriş kanalları
    private boolean emailNotifications = true;

    private boolean inAppNotifications = true;


    // Bildiriş kateqoriyaları
    private boolean newOpportunities = true;

    private boolean deadlineReminders = true;

    private boolean savedProjectChanges = true;

    private boolean platformUpdates = true;



    // İstifadəçinin maraqlandığı ölkələr
    @ElementCollection
    @CollectionTable(
            name = "notification_countries",
            joinColumns = @JoinColumn(name = "notification_settings_id")
    )
    @Column(name = "country")
    private Set<String> countries = new HashSet<>();


    // Kateqoriyalar
    @ElementCollection
    @CollectionTable(
            name = "notification_categories",
            joinColumns = @JoinColumn(name = "notification_settings_id")
    )
    @Column(name = "category")
    private Set<String> categories = new HashSet<>();


    // Online / Offline / Hybrid
    @ElementCollection
    @CollectionTable(
            name = "notification_formats",
            joinColumns = @JoinColumn(name = "notification_settings_id")
    )
    @Column(name = "format")
    private Set<String> formats = new HashSet<>();


    // 1,3,7 gün əvvəl
    private Integer deadlineReminderDays;
    @ElementCollection
    @CollectionTable(
            name = "notification_project_types",
            joinColumns = @JoinColumn(name = "notification_settings_id")
    )
    @Column(name = "project_type")
    private Set<String> projectTypes = new HashSet<>();


    private String duration;


}