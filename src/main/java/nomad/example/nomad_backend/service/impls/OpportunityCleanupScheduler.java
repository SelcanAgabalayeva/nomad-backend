package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OpportunityCleanupScheduler {

    private final OpportunityRepository opportunityRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredOpportunities() {

        LocalDate today = LocalDate.now();

        opportunityRepository.deleteByDeadlineBefore(today);

        System.out.println(
                "Deadline-i keçmiş opportunity-lər silindi. Tarix: " + today
        );
    }
}
