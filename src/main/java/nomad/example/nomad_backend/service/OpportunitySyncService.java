package nomad.example.nomad_backend.service;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.config.GoogleSheetsService;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.OpportunityStatus;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private boolean isChanged(String a, String b) {

        if (a == null && b == null) {
            return false;
        }

        if (a == null || b == null) {
            return true;
        }

        return !a.equalsIgnoreCase(b);
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
        List<List<Object>> rows = googleSheetsService.read("Sheet1!A2:U1000");

        System.out.println("ROW COUNT: " + rows.size());

        List<String> sheetKeys = new ArrayList<>();

        System.out.println("ROW COUNT: " + rows.size());

        for (List<Object> row : rows) {

            System.out.println("ROW DATA: " + row);
            System.out.println("ROW SIZE: " + row.size());

            if (cell(row, 1).isBlank()) {
                System.out.println("SKIPPED EMPTY ROW");
                continue;
            }

            String ss = cell(row,0);

            String title = cell(row,1)
                    .replace("\n","")
                    .trim();

            String deadline = cell(row,2).trim();

            String eventDateRange = cell(row,3);
            String duration = cell(row,4);
            String type = cell(row,5);
            String volunteeringType = cell(row,6);
            String ageRequirement = cell(row,7);
            String category = cell(row,8);

            String sumAz = cell(row,9);
            String sumEn = cell(row,10);
            String sumRus = cell(row,11);

            String city = cell(row,12);
            String financialSupport = cell(row,13);
            String language = cell(row,14);
            String typeDetail = cell(row,15);
            String country = cell(row,16);
            String applyLink = cell(row,17);
            String escOrSalto = cell(row,18);
            String openingDate = cell(row,19);
            String status = cell(row,20);
            String uniqueKey = title + "_" + deadline;
            sheetKeys.add(uniqueKey);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy");

            LocalDate deadlineDate = null;

            if (!deadline.isBlank() &&
                    deadline.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {

                deadlineDate = LocalDate.parse(deadline, formatter);
            }


            Opportunity oldOpportunity =
                    opportunityRepository.findByUniqueKey(uniqueKey)
                            .orElse(null);

            boolean isNew = oldOpportunity == null;

            Opportunity opportunity =
                    isNew ? new Opportunity() : oldOpportunity;
            boolean changed = false;

            if (oldOpportunity != null) {

                changed =
                        !java.util.Objects.equals(oldOpportunity.getDeadline(), deadlineDate)
                                || isChanged(oldOpportunity.getCategory(), category)
                                || isChanged(oldOpportunity.getCountry(), country)
                                || isChanged(oldOpportunity.getTitle(), title)
                                || isChanged(oldOpportunity.getCity(), city)
                                || isChanged(oldOpportunity.getDuration(), duration)
                                || isChanged(oldOpportunity.getLanguage(), language)
                                || isChanged(oldOpportunity.getType(), type)
                                || isChanged(oldOpportunity.getTypeDetail(), typeDetail)
                                || isChanged(oldOpportunity.getApplyLink(), applyLink)
                                || isChanged(oldOpportunity.getVolunteeringType(), volunteeringType)
                                || isChanged(oldOpportunity.getAgeRequirement(), ageRequirement)
                                || isChanged(oldOpportunity.getFinancialSupport(), financialSupport)
                                || isChanged(oldOpportunity.getEventDateRange(), eventDateRange)
                                || isChanged(oldOpportunity.getEscOrSalto(), escOrSalto);
            }


            opportunity.setTitle(title);
            opportunity.setDeadline(deadlineDate);
            opportunity.setType(type);
            opportunity.setCategory(category);
            opportunity.setSumAz(sumAz);
            opportunity.setSumEn(sumEn);
            opportunity.setSumRus(sumRus);
            opportunity.setTypeDetail(typeDetail);
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
            opportunity.setActive(true);
            if (status.isBlank()) {
                opportunity.setStatus(OpportunityStatus.READY);
            } else {
                opportunity.setStatus(
                        OpportunityStatus.valueOf(status.toUpperCase())
                );
            }
            Opportunity savedOpportunity =
                    opportunityRepository.save(opportunity);


            if (isNew) {

                try {
                    notificationService.notifyInterestedUsers(savedOpportunity);
                } catch (Exception e) {
                    System.out.println("Notification error: " + e.getMessage());
                }

            } else if (changed) {

                try {
                    notificationService.notifySavedProjectChanges(savedOpportunity);
                } catch (Exception e) {
                    System.out.println("Notification error: " + e.getMessage());
                }

            }

        }


        opportunityRepository.findAll()
                .stream()
                .filter(op -> !sheetKeys.contains(op.getUniqueKey()))
                .forEach(op -> {
                    System.out.println("DEACTIVATED: " + op.getTitle());
                    op.setActive(false);
                    op.setStatus(OpportunityStatus.DRAFT);
                    opportunityRepository.save(op);
                });
    }
}

