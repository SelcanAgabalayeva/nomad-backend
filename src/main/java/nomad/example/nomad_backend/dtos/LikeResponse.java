package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LikeResponse {

    private Long id;
    private LocalDateTime createdAt;
    private OpportunityResponse opportunity;
}
