package nomad.example.nomad_backend.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MundusProgramRequest {

    private String programName;
    private String description;
    private String category;
    private String fieldOfStudy;

    @Builder.Default
    private List<String> countries = new ArrayList<>();

    @Builder.Default
    private List<String> universities = new ArrayList<>();

    @Builder.Default
    private List<String> bachelorFields = new ArrayList<>();

    @Builder.Default
    private List<String> requiredDocuments = new ArrayList<>();

    private String degree;
    private String duration;
    private String language;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;
    @JsonFormat(pattern = "yyyy-MM-dd")
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

}
