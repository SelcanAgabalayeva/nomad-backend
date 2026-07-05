package nomad.example.nomad_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "opportunities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String deadline;

    private String type;

    private String category;

    @Column(length = 2000)
    private String sumAz;

    @Column(length = 2000)
    private String sumEn;

    @Column(unique = true)
    private String uniqueKey; // duplicate control üçün
}
