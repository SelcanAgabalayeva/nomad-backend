package nomad.example.nomad_backend.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponseDto {

    private int status;

    private String message;
}
