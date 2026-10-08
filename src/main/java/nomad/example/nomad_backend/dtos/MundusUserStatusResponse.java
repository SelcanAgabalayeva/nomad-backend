package nomad.example.nomad_backend.dtos;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MundusUserStatusResponse {

    private boolean favorite;

    private boolean saved;
}
