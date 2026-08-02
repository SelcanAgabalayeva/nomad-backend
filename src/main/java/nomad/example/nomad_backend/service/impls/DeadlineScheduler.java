package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.entity.NotificationSettings;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.NotificationSettingsRepository;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.repository.ProjectRepository;

@Component
@RequiredArgsConstructor
public class DeadlineScheduler {

    private final ProjectRepository projectRepository;
    private final EmailService emailService;
    private final NotificationSettingsRepository notificationSettingsRepository;


    @Scheduled(cron = "0 0 9 * * *")
    public void checkDeadlines() {


        List<UserProject> projects =
                projectRepository.findByStatus(ProjectStatus.SAVED);


        for (UserProject project : projects) {


            User user = project.getUser();


            NotificationSettings settings =
                    notificationSettingsRepository
                            .findByUserId(user.getId())
                            .orElse(null);


            if (settings == null) {
                continue;
            }


            if (!settings.isDeadlineReminders()) {
                continue;
            }


            int days =
                    settings.getDeadlineReminderDays() != null
                            ? settings.getDeadlineReminderDays()
                            : 1;


            LocalDate targetDate =
                    LocalDate.now().plusDays(days);



            if (project.getOpportunity()
                    .getDeadline()
                    .equals(targetDate)) {


                if (settings.isEmailNotifications()) {

                    emailService.sendDeadlineReminder(
                            user.getEmail(),
                            project.getOpportunity().getTitle()
                    );
                }
            }
        }
    }
}