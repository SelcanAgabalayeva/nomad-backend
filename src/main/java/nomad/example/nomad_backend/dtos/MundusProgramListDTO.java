package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MundusProgramListDTO {

    private Long id;
    private String programName;
    private String category;
    private String fieldOfStudy;
    private String degree;
    private LocalDate deadline;
    private LocalDate applicationOpens;
    private Boolean scholarship;
    private String applyLink;
    private String scholarshipAmount;
    private List<String> countries;

    // JPQL constructor projection üçün:
    // Repository-dən 10 parametr gəlir, countries sonradan doldurulur.
    public MundusProgramListDTO(
            Long id,
            String programName,
            String category,
            String fieldOfStudy,
            String degree,
            LocalDate deadline,
            LocalDate applicationOpens,
            Boolean scholarship,
            String applyLink,
            String scholarshipAmount
    ) {
        this.id = id;
        this.programName = programName;
        this.category = category;
        this.fieldOfStudy = fieldOfStudy;
        this.degree = degree;
        this.deadline = deadline;
        this.applicationOpens = applicationOpens;
        this.scholarship = scholarship;
        this.applyLink = applyLink;
        this.scholarshipAmount = scholarshipAmount;
        this.countries = new ArrayList<>();
    }
}
