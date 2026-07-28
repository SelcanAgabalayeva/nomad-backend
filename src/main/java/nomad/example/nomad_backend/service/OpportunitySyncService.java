package nomad.example.nomad_backend.service;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.config.GoogleSheetsService;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OpportunitySyncService {

    private final GoogleSheetsService googleSheetsService;
    private final OpportunityRepository opportunityRepository;
    private final NotificationService notificationService;
    private String cell(List<Object> row, int index) {
        return index < row.size()
                ? row.get(index).toString().trim()
                : "";
    }
    @Scheduled(fixedRate = 60000)
    public void sync() throws Exception {

        System.out.println("SYNC STARTED");

        List<List<Object>> rows = googleSheetsService.read("Sheet1!A2:L100");

        System.out.println("ROW COUNT: " + rows.size());

        for (List<Object> row : rows) {

            System.out.println("ROW DATA: " + row);
            System.out.println("ROW SIZE: " + row.size());

            if (cell(row, 1).isBlank()) {
                System.out.println("SKIPPED EMPTY ROW");
                continue;
            }

            String ss = cell(row, 0);

            String title = cell(row, 1);

            String deadline = cell(row, 2);

            String type = cell(row, 3);

            String category = cell(row, 4);

            String sumAz = cell(row, 5);

            String sumEn = cell(row, 6);

            String sumRus = cell(row, 7);

            String sort = cell(row, 8);

            String country = cell(row, 9);

            String applyLink = cell(row, 10);

            String openingDate = cell(row, 11);

            String uniqueKey = title + "_" + deadline;

            boolean isNew = opportunityRepository
                    .findByUniqueKey(uniqueKey)
                    .isEmpty();

            Opportunity opportunity = opportunityRepository
                    .findByUniqueKey(uniqueKey)
                    .orElse(new Opportunity());


            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalDate deadlineDate = null;

            if (!deadline.isBlank() &&
                    deadline.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {

                deadlineDate = LocalDate.parse(deadline, formatter);
            }


            opportunity.setTitle(title);
            opportunity.setDeadline(deadlineDate);
            opportunity.setType(type);
            opportunity.setCategory(category);
            opportunity.setSumAz(sumAz);
            opportunity.setSumEn(sumEn);
            opportunity.setSumRus(sumRus);
            opportunity.setSort(sort);
            opportunity.setCountry(country);
            opportunity.setApplyLink(applyLink);
            opportunity.setUniqueKey(uniqueKey);
            opportunity.setCountry(country);
            opportunity.setApplyLink(applyLink);

            LocalDate openingDateValue = null;

            if (!openingDate.isBlank() &&
                    openingDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {

                openingDateValue = LocalDate.parse(openingDate, formatter);
            }

            opportunity.setOpeningDate(openingDateValue);

            System.out.println("----------------------------");
            System.out.println("TITLE: " + title);
            System.out.println("DEADLINE: " + deadline);
            System.out.println("UNIQUE KEY: " + uniqueKey);
            System.out.println("----------------------------");
            Opportunity savedOpportunity =
                    opportunityRepository.save(opportunity);


            if (isNew) {
                notificationService.notifyInterestedUsers(savedOpportunity);
            }
        }
    }
}
