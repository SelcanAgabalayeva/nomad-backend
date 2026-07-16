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

    @Scheduled(fixedRate = 60000)
    public void sync() throws Exception {

        System.out.println("SYNC STARTED");

        List<List<Object>> rows = googleSheetsService.read("Sheet1!A2:L100");

        System.out.println("ROW COUNT: " + rows.size());

        for (List<Object> row : rows) {

            System.out.println("ROW DATA: " + row);
            System.out.println("ROW SIZE: " + row.size());

            if(row.size() < 12) {
                System.out.println("SKIPPED: " + row.size());
                continue;
            }

            String ss = row.get(0).toString();

            String title = row.get(1).toString();

            String deadline = row.get(2).toString();

            String type = row.get(3).toString();

            String category = row.get(4).toString();

            String sumAz = row.get(5).toString();

            String sumEn = row.get(6).toString();
            String sumRus = row.get(7).toString();
            String sort = row.get(8).toString();
            String country = row.get(9).toString();

            String applyLink = row.get(10).toString();
            String openingDate = row.get(11).toString();

            String uniqueKey = title + "_" + deadline;

            Opportunity opportunity = opportunityRepository
                    .findByUniqueKey(uniqueKey)
                    .orElse(new Opportunity());


            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalDate deadlineDate = null;

            if (deadline != null) {
                deadline = deadline.trim();

                if (deadline.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                    deadlineDate = LocalDate.parse(deadline, formatter);
                }
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

            if (openingDate != null) {
                openingDate = openingDate.trim();

                if (openingDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                    openingDateValue = LocalDate.parse(openingDate, formatter);
                }
            }

            opportunity.setOpeningDate(openingDateValue);


            opportunityRepository.save(opportunity);
        }
    }
}
