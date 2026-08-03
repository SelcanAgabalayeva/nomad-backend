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

    @Builder.Default
    private boolean emailNotifications = true;

    @Builder.Default
    private boolean inAppNotifications = true;

    @Builder.Default
    private boolean newOpportunities = true;

    @Builder.Default
    private boolean deadlineReminders = true;

    @Builder.Default
    private boolean savedProjectChanges = true;

    @Builder.Default
    private boolean platformUpdates = true;

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "notification_countries",
            joinColumns = @JoinColumn(name = "notification_settings_id")
    )
    @Column(name = "country")
    private Set<String> countries = new HashSet<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "notification_categories",
            joinColumns = @JoinColumn(name = "notification_settings_id")
    )
    @Column(name = "category")
    private Set<String> categories = new HashSet<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "notification_formats",
            joinColumns = @JoinColumn(name = "notification_settings_id")
    )
    @Column(name = "format")
    private Set<String> formats = new HashSet<>();


    private Integer deadlineReminderDays;
    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "notification_project_types",
            joinColumns = @JoinColumn(name = "notification_settings_id")
    )
    @Column(name = "project_type")
    private Set<String> projectTypes = new HashSet<>();


    private String duration;


}