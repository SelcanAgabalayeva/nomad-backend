package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
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

    @Scheduled(cron = "0 0 9 * * *")
    public void checkDeadlines() {

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<UserProject> projects =
                projectRepository.findByStatus(ProjectStatus.SAVED);

        for (UserProject project : projects) {

            if (project.getOpportunity().getDeadline().equals(tomorrow)) {

                emailService.sendDeadlineReminder(
                        project.getUser().getEmail(),
                        project.getOpportunity().getTitle()
                );
            }
        }
    }
}