package nomad.example.nomad_backend.dtos;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformStatsResponse {
    private String activeOpportunities;
    private String servicesCount;
    private String availability;
    private long categoriesCount;
}