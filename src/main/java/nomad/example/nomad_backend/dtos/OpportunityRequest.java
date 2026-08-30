package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityRequest {

    private String title;
    private LocalDate deadline;
    private String type;
    private String category;

    private String sumAz;
    private String sumEn;
    private String sumRus;

    private String typeDetail;
    private String country;
    private String applyLink;
    private LocalDate openingDate;

    private String uniqueKey;

    private String duration;
    private String language;
    private String city;

    private String escOrSalto;
    private String eventDateRange;
    private String financialSupport;
    private String ageRequirement;
    private String volunteeringType;
}
