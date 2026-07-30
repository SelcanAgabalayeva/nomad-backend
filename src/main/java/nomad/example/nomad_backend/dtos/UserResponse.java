package nomad.example.nomad_backend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nomad.example.nomad_backend.entity.Role;
import nomad.example.nomad_backend.enums.EducationLevel;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
    private String university;
    private String major;
    private EducationLevel educationLevel;
    private Set<String> interests;
    private boolean newsletter;
    private String profileImageUrl;
    private Role role;
    private boolean emailVerified;

}
