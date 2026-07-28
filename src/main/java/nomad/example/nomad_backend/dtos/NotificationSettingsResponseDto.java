package nomad.example.nomad_backend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NotificationSettingsResponseDto {


    private boolean emailNotifications;

    private boolean inAppNotifications;

    private boolean newOpportunities;

    private boolean deadlineReminders;

    private boolean savedProjectChanges;

    private boolean platformUpdates;

}
