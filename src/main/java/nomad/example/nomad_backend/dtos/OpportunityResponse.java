package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpportunityResponse {

    private Long id;
    private String title;
    private String country;
    private LocalDate deadline;
    private String type;
    private String category;
    private String applyLink;
}