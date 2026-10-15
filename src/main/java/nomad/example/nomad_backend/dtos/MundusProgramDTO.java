package nomad.example.nomad_backend.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MundusProgramDTO {

    private Long id;
    private String programName;
    private String description;
    private String category;
    private String fieldOfStudy;

    private List<String> countries;
    private List<String> universities;
    private List<String> bachelorFields;
    private List<String> requiredDocuments;

    private String degree;
    private String duration;
    private String language;

    private LocalDate deadline;
    private LocalDate applicationOpens;
    private String intake;

    private String ieltsRequirement;
    private Double ieltsScore;
    private Boolean ieltsRequired;

    private String toeflRequirement;
    private Double toeflScore;

    private Boolean scholarship;
    private String scholarshipAmount;
    private String applicationFee;

    private String officialWebsite;
    private String applyLink;

    private Boolean active;
    private Long viewCount;
    private Long applicationClickCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
