package nomad.example.nomad_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    private String tagline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "organization_categories",
            joinColumns = @JoinColumn(name = "organization_id")
    )
    @Column(name = "category")
    private List<String> categories;

    private String website;

    private String instagram;

    private String facebook;

    private String email;

    private String location;

    private String logo;
}