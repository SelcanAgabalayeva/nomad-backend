package nomad.example.nomad_backend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nomad.example.nomad_backend.enums.EducationLevel;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class ProfileResponse {


    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String profileImageUrl;


    private String phoneNumber;

    private String country;

    private String city;

    private String university;

    private String major;

    private String bio;

    private LocalDate birthDate;


    private EducationLevel educationLevel;


    private boolean profileCompleted;

}
