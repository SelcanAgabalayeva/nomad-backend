package nomad.example.nomad_backend.dtos;


import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import nomad.example.nomad_backend.enums.EducationLevel;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class RegisterRequestDto {

        @NotBlank
        private String firstName;

        @NotBlank
        private String lastName;


        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 8,
                message = "Password must be at least 8 characters")
        private String password;

        @NotBlank
        private String confirmPassword;

        @NotBlank
        private String phoneNumber;

        @Past
        private LocalDate birthDate;

        private String university;

        private String major;

        private EducationLevel educationLevel;

        private Set<String> interests;

        @AssertTrue(message = "Şərtləri qəbul etməlisiniz.")
        private boolean termsAccepted;

        private boolean newsletter;
    }


