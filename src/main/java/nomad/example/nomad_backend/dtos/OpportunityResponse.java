package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nomad.example.nomad_backend.entity.DurationType;
import nomad.example.nomad_backend.entity.VisaType;

import java.time.LocalDate;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpportunityResponse {

    private Long id;
    private String title;

    private String country;
    private String city;
    private String duration;
    private DurationType durationType;
    private VisaType visaType;

    private LocalDate deadline;
    private String type;
    private String category;
    private String applyLink;
}