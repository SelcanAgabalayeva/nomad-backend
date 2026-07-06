package nomad.example.nomad_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class GoogleProfileCompleteRequest {
    @NotBlank(message = "ERROR_EMAIL_REQUIRED")
    @Email(message = "ERROR_INVALID_EMAIL_FORMAT")
    private String email;

    private String phoneNumber;
    private LocalDate birthDate;
    private String university;
    private String educationLevel;
    private String major;
    private Boolean isSubscribedToEmails;
}