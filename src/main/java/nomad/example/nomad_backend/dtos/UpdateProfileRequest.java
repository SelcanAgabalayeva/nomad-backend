package nomad.example.nomad_backend.dtos;


import lombok.Getter;
import lombok.Setter;
import nomad.example.nomad_backend.enums.EducationLevel;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateProfileRequest {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String country;

    private String city;

    private String university;

    private String major;

    private EducationLevel educationLevel;

    private LocalDate birthDate;


}
