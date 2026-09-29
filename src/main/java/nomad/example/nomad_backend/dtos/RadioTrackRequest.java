package nomad.example.nomad_backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RadioTrackRequest {

    private String title;

    private String artist;

    private String coverUrl;

    private Integer position;
}
