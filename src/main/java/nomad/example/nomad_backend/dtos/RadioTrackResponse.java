package nomad.example.nomad_backend.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RadioTrackResponse {

    private Long id;
    private String title;
    private String artist;
    private String audioUrl;
    private String coverUrl;
    private Integer position;
}
