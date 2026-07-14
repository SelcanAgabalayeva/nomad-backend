package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String sort;
    private LocalDate deadline;
    private LocalDate openingDate;

    private String description;
    private String applyLink;   // "Müraciət et" düyməsinə kliklədikdə açılacaq link
    private boolean isFavorite;
    private boolean isSaved;
    private boolean isApplied;
}
