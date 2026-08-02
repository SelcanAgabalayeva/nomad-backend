package nomad.example.nomad_backend.dtos;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class NotificationSettingsResponseDto {


    private boolean emailNotifications;

    private boolean inAppNotifications;

    private boolean newOpportunities;

    private boolean deadlineReminders;

    private boolean savedProjectChanges;

    private boolean platformUpdates;
    private Set<String> countries;

    private Set<String> categories;

    private Set<String> formats;

    private Set<String> projectTypes;

    private Integer deadlineReminderDays;

    private String duration;
}
