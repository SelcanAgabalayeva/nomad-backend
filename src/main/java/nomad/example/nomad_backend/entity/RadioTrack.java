package nomad.example.nomad_backend.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "radio_tracks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RadioTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column(name = "audio_url", nullable = false, columnDefinition = "TEXT")
    private String audioUrl;

    @Column(name = "cover_url", columnDefinition = "TEXT")
    private String coverUrl;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
