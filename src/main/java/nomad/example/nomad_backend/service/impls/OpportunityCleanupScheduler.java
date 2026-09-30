package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import nomad.example.nomad_backend.entity.Opportunity;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OpportunityCleanupScheduler {

    private final OpportunityRepository opportunityRepository;

    private static final DateTimeFormatter EVENT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Scheduled(
            cron = "0 0 0 * * *",
            zone = "Asia/Baku"
    )
    @Transactional
    public void deactivateExpiredOpportunities() {

        List<Opportunity> opportunities =
                opportunityRepository.findByActiveTrue();

        LocalDate today = LocalDate.now();

        for (Opportunity opportunity : opportunities) {

            LocalDate expirationDate;

            if (opportunity.getDeadline() != null) {
                expirationDate = opportunity.getDeadline();
            } else {
                expirationDate = extractEventEndDate(
                        opportunity.getEventDateRange()
                );
            }

            if (expirationDate != null
                    && expirationDate.isBefore(today)) {

                opportunity.setActive(false);
            }
        }

        opportunityRepository.saveAll(opportunities);
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