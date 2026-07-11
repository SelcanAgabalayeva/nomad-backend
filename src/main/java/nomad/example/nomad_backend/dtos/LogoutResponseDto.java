package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LogoutResponseDto {

    private boolean success;

    private String message;
}
