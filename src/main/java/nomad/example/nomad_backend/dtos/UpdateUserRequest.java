package nomad.example.nomad_backend.dtos;

import lombok.Getter;
import lombok.Setter;
import nomad.example.nomad_backend.enums.EducationLevel;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class UpdateUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private String university;
    private String major;
    private EducationLevel educationLevel;
    private Set<String> interests;
    private Boolean newsletter;

}
