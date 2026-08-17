package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nomad.example.nomad_backend.entity.DurationType;
import nomad.example.nomad_backend.entity.VisaType;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityDetailResponse {
    private Long id;
    private String title;
    private String country;
    private String type;
    private String category;
    private String typeDetail;
    private LocalDate deadline;
    private LocalDate openingDate;

    private String description;
    private String applyLink;
    private boolean isFavorite;
    private boolean isSaved;
    private boolean isApplied;

    private String duration;
    private String language;
    private String eventDateRange;
    private String financialSupport;

    private String city;
    private String volunteeringType;
    private String ageRequirement;
    private String escOrSalto;

    private DurationType durationType;
    private VisaType visaType;
}

