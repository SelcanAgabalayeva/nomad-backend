package nomad.example.nomad_backend.service;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.config.GoogleSheetsService;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpportunitySyncService {

    private final GoogleSheetsService googleSheetsService;
    private final OpportunityRepository opportunityRepository;

    @Scheduled(fixedRate = 60000)
    public void sync() throws Exception {

        List<List<Object>> rows = googleSheetsService.read("Sheet1!A2:F");

        for (List<Object> row : rows) {

            String title = row.get(0).toString();
            String deadline = row.get(1).toString();
            String type = row.get(2).toString();
            String category = row.get(3).toString();
            String sumAz = row.get(4).toString();
            String sumEn = row.get(5).toString();

            String uniqueKey = title + "_" + deadline;

            Opportunity opportunity = opportunityRepository
                    .findByUniqueKey(uniqueKey)
                    .orElse(new Opportunity());

            opportunity.setTitle(title);
            opportunity.setDeadline(deadline);
            opportunity.setType(type);
            opportunity.setCategory(category);
            opportunity.setSumAz(sumAz);
            opportunity.setSumEn(sumEn);
            opportunity.setUniqueKey(uniqueKey);

            opportunityRepository.save(opportunity);
        }
    }
}
