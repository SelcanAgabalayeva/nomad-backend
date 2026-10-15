package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
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
    private String scholarshipAmount;
}
