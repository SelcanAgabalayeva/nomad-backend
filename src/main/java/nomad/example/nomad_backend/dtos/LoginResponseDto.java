package nomad.example.nomad_backend.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponseDto {
    private String token;
    private String role;
    private String refreshToken;
}
