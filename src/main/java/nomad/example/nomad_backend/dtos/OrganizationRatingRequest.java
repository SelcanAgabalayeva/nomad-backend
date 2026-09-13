package nomad.example.nomad_backend.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationRatingRequest {

    private Long userId;

    private Double rating;
}
