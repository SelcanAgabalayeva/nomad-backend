package nomad.example.nomad_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nomad.example.nomad_backend.entity.DurationType;
import nomad.example.nomad_backend.entity.VisaType;

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
    private String typeDetail;
    private LocalDate deadline;
    private LocalDate openingDate;
    private long daysLeft; // Qalan gün sayı

    // İstifadəçinin bu karta qarşı olan statusu
    private boolean isSaved;   // Şəkildəki bookmark ikonu üçün
    private boolean isApplied; // "Müraciət et" düyməsinin aktiv/passivliyi üçün
    private String escOrSalto;
    private String volunteeringType;
    private String applyLink;
    private String duration;
    private DurationType durationType;
    private VisaType visaType;

}