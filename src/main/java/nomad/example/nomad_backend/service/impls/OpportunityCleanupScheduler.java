package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import nomad.example.nomad_backend.entity.Opportunity;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpportunityCleanupScheduler {

    private final OpportunityRepository opportunityRepository;

    private static final DateTimeFormatter EVENT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteExpiredOpportunities() {

        LocalDate today = LocalDate.now();

        // 1. Deadline-i keçmiş opportunity-ləri sil
        opportunityRepository.deleteByDeadlineBefore(today);

        // 2. Deadline-i olmayan, amma event tarixi keçmiş opportunity-ləri sil
        List<Opportunity> opportunities = opportunityRepository.findAll();

        for (Opportunity opportunity : opportunities) {

            // Deadline varsa, artıq yuxarıdakı query idarə edir
            if (opportunity.getDeadline() != null) {
                continue;
            }

            LocalDate eventEndDate =
                    extractEventEndDate(opportunity.getEventDateRange());

            if (eventEndDate != null && eventEndDate.isBefore(today)) {

                opportunityRepository.delete(opportunity);

                System.out.println(
                        "Event tarixi keçmiş opportunity silindi: "
                                + opportunity.getTitle()
                                + " | Event: "
                                + opportunity.getEventDateRange()
                );
            }
        }

        System.out.println(
                "Opportunity cleanup tamamlandı. Tarix: " + today
        );
    }

    private LocalDate extractEventEndDate(String eventDateRange) {

        if (eventDateRange == null || eventDateRange.isBlank()) {
            return null;
        }

        try {
            String value = eventDateRange.trim();

            // Məsələn:
            // 19/10/2026 - 24/10/2026
            if (value.contains("-")) {

                String[] dates = value.split("-");

                if (dates.length == 2) {
                    String endDate = dates[1].trim();

                    return LocalDate.parse(
                            endDate,
                            EVENT_DATE_FORMAT
                    );
                }
            }

            // Məsələn:
            // 22/09/2026
            return LocalDate.parse(
                    value,
                    EVENT_DATE_FORMAT
            );

        } catch (DateTimeParseException e) {

            System.out.println(
                    "Event tarixi parse edilə bilmədi: "
                            + eventDateRange
            );

            return null;
        }
    }
}