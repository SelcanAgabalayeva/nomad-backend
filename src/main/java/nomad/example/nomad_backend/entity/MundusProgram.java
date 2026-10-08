package nomad.example.nomad_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mundus_programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MundusProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String programName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 150)
    private String category;

    @Column(length = 200)
    private String fieldOfStudy;

    @ElementCollection
    @CollectionTable(
            name = "mundus_program_countries",
            joinColumns = @JoinColumn(name = "program_id")
    )
    @Column(name = "country")
    @Builder.Default
    private List<String> countries = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "mundus_program_universities",
            joinColumns = @JoinColumn(name = "program_id")
    )
    @Column(name = "university")
    @Builder.Default
    private List<String> universities = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "mundus_program_bachelor_fields",
            joinColumns = @JoinColumn(name = "program_id")
    )
    @Column(name = "bachelor_field")
    @Builder.Default
    private List<String> bachelorFields = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "mundus_program_documents",
            joinColumns = @JoinColumn(name = "program_id")
    )
    @Column(name = "document")
    @Builder.Default
    private List<String> requiredDocuments = new ArrayList<>();

    @Column(length = 100)
    private String degree;

    @Column(length = 100)
    private String duration;

    @Column(length = 100)
    private String language;

    private LocalDate deadline;

    private LocalDate applicationOpens;

    @Column(length = 150)
    private String intake;

    @Column(length = 200)
    private String ieltsRequirement;

    private Double ieltsScore;

    @Builder.Default
    @Column(nullable = false)
    private Boolean ieltsRequired = false;

    @Column(length = 200)
    private String toeflRequirement;

    private Double toeflScore;

    @Builder.Default
    @Column(nullable = false)
    private Boolean scholarship = false;

    @Column(length = 300)
    private String scholarshipAmount;

    @Column(length = 150)
    private String applicationFee;

    @Column(length = 1000)
    private String officialWebsite;

    @Column(length = 1000)
    private String applyLink;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Column(nullable = false)
    private Long viewCount = 0L;

    @Builder.Default
    @Column(nullable = false)
    private Long applicationClickCount = 0L;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        updatedAt = now;

        if (active == null) {
            active = true;
        }

        if (scholarship == null) {
            scholarship = false;
        }

        if (ieltsRequired == null) {
            ieltsRequired = false;
        }

        if (viewCount == null) {
            viewCount = 0L;
        }

        if (applicationClickCount == null) {
            applicationClickCount = 0L;
        }

        if (countries == null) {
            countries = new ArrayList<>();
        }

        if (universities == null) {
            universities = new ArrayList<>();
        }

        if (bachelorFields == null) {
            bachelorFields = new ArrayList<>();
        }

        if (requiredDocuments == null) {
            requiredDocuments = new ArrayList<>();
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}


