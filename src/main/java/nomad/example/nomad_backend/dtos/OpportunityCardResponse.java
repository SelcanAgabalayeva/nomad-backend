package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpportunityCardResponse {
    private Long id;
    private String title;
    private String country;
    private String type;
    private String category;
    private String sort;
    private LocalDate deadline;
    private LocalDate openingDate;
    private long daysLeft; // Qalan gün sayı

    // İstifadəçinin bu karta qarşı olan statusu
    private boolean isSaved;   // Şəkildəki bookmark ikonu üçün
    private boolean isApplied; // "Müraciət et" düyməsinin aktiv/passivliyi üçün
}