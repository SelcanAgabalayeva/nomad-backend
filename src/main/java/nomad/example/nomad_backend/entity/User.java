package nomad.example.nomad_backend.entity;
import jakarta.persistence.*;
import lombok.*;
import nomad.example.nomad_backend.enums.EducationLevel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;


    private String password;
    @Column(unique = true)
    private String phoneNumber;

    private LocalDate birthDate;

    private String university;

    private String major;

    @Enumerated(EnumType.STRING)
    private EducationLevel educationLevel;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_interests")
    @Column(name = "interest")
    private Set<String> interests = new HashSet<>();

    private boolean termsAccepted;
    @OneToMany(mappedBy = "user")
    private List<UserProject> projects;
    private boolean newsletter;
    @Column(nullable = false)
    private String provider;

}

