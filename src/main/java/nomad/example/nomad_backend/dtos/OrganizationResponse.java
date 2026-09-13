package nomad.example.nomad_backend.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationResponse {

    private Long id;

    private String name;

    private String slug;

    private String tagline;

    private String description;

    private List<String> categories;

    private String website;

    private String instagram;

    private String facebook;

    private String email;

    private String location;

    private String logo;
    private Double rating;
    private Long reviewCount;
}
