package nomad.example.nomad_backend.dtos;

import lombok.*;

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

}
