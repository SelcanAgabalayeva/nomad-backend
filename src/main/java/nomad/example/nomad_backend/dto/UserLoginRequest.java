package nomad.example.nomad_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank(message = "ERROR_EMAIL_REQUIRED")
    @Email(message = "ERROR_INVALID_EMAIL_FORMAT")
    private String email;

    @NotBlank(message = "ERROR_PASSWORD_REQUIRED")
    private String password;
}