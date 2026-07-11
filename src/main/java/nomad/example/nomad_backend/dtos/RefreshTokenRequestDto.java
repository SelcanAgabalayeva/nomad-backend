package nomad.example.nomad_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDto {
    @NotBlank
    private String refreshToken;
}

