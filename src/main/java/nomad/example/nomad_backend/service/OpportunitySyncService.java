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

        // QEYD: cədvələ yeni sütunlar D-L arasına daxil edildiyi üçün
        // BÜTÜN indekslər (təkcə yeni sahələr yox) yenidən uyğunlaşdırıldı.
        // Real sütun sırası (A-T):
        // 0 ss | 1 title | 2 deadline | 3 eventDateRange | 4 duration |
        // 5 type | 6 volunteeringType | 7 ageRequirement | 8 category |
        // 9 sumAz | 10 sumEn | 11 sumRus | 12 city | 13 financialSupport |
        // 14 language | 15 sort(format) | 16 country | 17 applyLink |
        // 18 escOrSalto | 19 openingDate
        List<List<Object>> rows = googleSheetsService.read("Sheet1!A2:T1000");

        System.out.println("ROW COUNT: " + rows.size());

        for (List<Object> row : rows) {

            System.out.println("ROW DATA: " + row);
            System.out.println("ROW SIZE: " + row.size());

            if (cell(row, 1).isBlank()) {
                System.out.println("SKIPPED EMPTY ROW");
                continue;
            }

            String title = cell(row, 0);
            String deadline = cell(row, 1);
            String eventDateRange = cell(row, 2);

            String duration = cell(row, 3);

            String type = cell(row, 4);

            String volunteeringType = cell(row, 5);

            String ageRequirement = cell(row, 6);

            String category = cell(row, 7);

            String sumAz = cell(row, 8);
            String sumEn = cell(row, 9);
            String sumRus = cell(row, 10);

            String city = cell(row, 11);

            String financialSupport = cell(row, 12);

            String language = cell(row, 13);

            String sort = cell(row, 14);

            String country = cell(row, 15);

            String applyLink = cell(row, 16);

            String escOrSalto = cell(row, 17);

            String openingDate = cell(row, 18);

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

            opportunity.setDuration(duration.isBlank() ? null : duration);
            opportunity.setLanguage(language.isBlank() ? null : language);
            opportunity.setEventDateRange(eventDateRange.isBlank() ? null : eventDateRange);
            opportunity.setFinancialSupport(financialSupport.isBlank() ? null : financialSupport);
            opportunity.setCity(city.isBlank() ? null : city);
            opportunity.setVolunteeringType(volunteeringType.isBlank() ? null : volunteeringType);
            opportunity.setAgeRequirement(ageRequirement.isBlank() ? null : ageRequirement);
            opportunity.setEscOrSalto(escOrSalto.isBlank() ? null : escOrSalto);

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

