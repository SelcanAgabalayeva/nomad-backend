package nomad.example.nomad_backend.dtos;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MundusProgramListDto {

    private Long id;
    private String programName;
    private String category;
    private String fieldOfStudy;
    private String degree;
    private LocalDate deadline;
    private Boolean scholarship;
    private Boolean active;
}
