package nomad.example.nomad_backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSettingsRequestDto {


    private boolean emailNotifications;

    private boolean inAppNotifications;

    private boolean newOpportunities;

    private boolean deadlineReminders;

    private boolean savedProjectChanges;

    private boolean platformUpdates;

}
