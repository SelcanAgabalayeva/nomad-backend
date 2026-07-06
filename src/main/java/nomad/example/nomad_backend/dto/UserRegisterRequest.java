package nomad.example.nomad_backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UserRegisterRequest {
    @NotBlank(message = "ERROR_FIRSTNAME_REQUIRED")
    private String firstName;

    @NotBlank(message = "ERROR_LASTNAME_REQUIRED")
    private String lastName;

    @NotBlank(message = "ERROR_EMAIL_REQUIRED")
    @Email(message = "ERROR_INVALID_EMAIL_FORMAT")
    private String email;

    @NotBlank(message = "ERROR_PASSWORD_REQUIRED")
    @Size(min = 6, message = "ERROR_PASSWORD_TOO_SHORT")
    private String password;

    private String phoneNumber;
    private LocalDate birthDate;
    private String university;
    private String educationLevel;
    private String major;
    private Boolean isSubscribedToEmails;
}