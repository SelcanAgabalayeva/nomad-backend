package nomad.example.nomad_backend.dtos;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthResponseDto {

    private boolean success;
    private String accessToken;

    private String refreshToken;
    private UserResponseDto user;

    private long expiresIn;
    private String message;
}

