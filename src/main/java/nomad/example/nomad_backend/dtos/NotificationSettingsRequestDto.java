package nomad.example.nomad_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import nomad.example.nomad_backend.entity.DurationType;

import java.util.Set;

@Getter
@Setter
public class NotificationSettingsRequestDto {


    private boolean emailNotifications;

    private boolean inAppNotifications;


    private boolean newOpportunities;

    private boolean deadlineReminders;

    private boolean savedProjectChanges;

    private boolean platformUpdates;


    private Set<String> countries;


    private Set<String> projectTypes;


    private Set<String> categories;


    private Set<String> formats;


    private Integer deadlineReminderDays;


    private DurationType duration;

}
