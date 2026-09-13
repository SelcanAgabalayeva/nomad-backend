package nomad.example.nomad_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "organization_ratings",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"organization_id", "user_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Double rating;
}
