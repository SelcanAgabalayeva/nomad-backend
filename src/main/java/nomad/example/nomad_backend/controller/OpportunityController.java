package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.OpportunityCardResponse;
import nomad.example.nomad_backend.dtos.OpportunityDetailResponse;
import nomad.example.nomad_backend.dtos.OpportunityResponse;
import nomad.example.nomad_backend.dtos.PlatformStatsResponse;
import nomad.example.nomad_backend.repository.OpportunityRepository;

import nomad.example.nomad_backend.service.ProjectService;

import nomad.example.nomad_backend.service.impls.DurationTypeService;
import nomad.example.nomad_backend.service.impls.VisaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityRepository repository;
    private final ProjectService projectService;

    private final DurationTypeService durationTypeService;
    private final VisaService visaService;



    @GetMapping
    public List<OpportunityResponse> getAll() {

        return repository.findAllByActiveTrueOrderByDeadlineAsc()
                .stream()
                .map(opportunity -> OpportunityResponse.builder()
                        .id(opportunity.getId())
                        .title(opportunity.getTitle())
                        .country(opportunity.getCountry())
                        .city(opportunity.getCity())
                        .duration(opportunity.getDuration())
                        .eventDateRange(opportunity.getEventDateRange())

                        .durationType(
                                durationTypeService.determine(
                                        opportunity.getDuration()
                                )
                        )

                        .visaType(
                                visaService.determine(
                                        opportunity.getCountry()
                                )
                        )

                        .deadline(opportunity.getDeadline())
                        .type(opportunity.getType())
                        .category(opportunity.getCategory())
                        .applyLink(opportunity.getApplyLink())

                        .build()
                )
                .toList();
    }


    @GetMapping("/paged")
    public Page<OpportunityResponse> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return repository
                .findAllByActiveTrueOrderByDeadlineAsc(pageable)
                .map(opportunity -> {
                    // FRONTEND HİYLƏSİ: null və ya "Hamısı" gələrsə "" (boş sətir) edirik
                    // Bu zaman JS-dəki `raw.typeDetail ?? "Hamısı"` işə düşməyəcək və tag itəcək
                    String typeDetail = opportunity.getTypeDetail();
                    if (typeDetail == null || "Hamısı".equalsIgnoreCase(typeDetail) || "Hamisi".equalsIgnoreCase(typeDetail)) {
                        typeDetail = "";
                    }

                    // ESC/SALTO böyük hərflə standartlaşdırılır ki, JS-dəki 'ESC' === 'ESC' şərti ödənsin
                    String escOrSalto = opportunity.getEscOrSalto() != null
                            ? opportunity.getEscOrSalto().trim().toUpperCase()
                            : null;

                    return OpportunityResponse.builder()
                            .id(opportunity.getId())
                            .title(opportunity.getTitle())
                            .country(opportunity.getCountry())
                            .city(opportunity.getCity())
                            .eventDateRange(opportunity.getEventDateRange())
                            .duration(opportunity.getDuration())
                            .escOrSalto(escOrSalto)
                            .volunteeringType(opportunity.getVolunteeringType())
                            .durationType(durationTypeService.determine(opportunity.getDuration()))
                            .visaType(visaService.determine(opportunity.getCountry()))
                            .deadline(opportunity.getDeadline())
                            .type(opportunity.getType())
                            .typeDetail(typeDetail) // Boş string kimi gedəcək
                            .category(opportunity.getCategory())
                            .applyLink(opportunity.getApplyLink())
                            .build();
                });
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<OpportunityDetailResponse> getDetails(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false, defaultValue = "az") String lang
    ) {

        return ResponseEntity.ok(
                projectService.getOpportunityDetails(id, userId, lang)
        );
    }


    @GetMapping("/stats")
    public ResponseEntity<PlatformStatsResponse> getStats() {
        return ResponseEntity.ok(
                projectService.getPlatformStatistics()
        );
    }


    @GetMapping("/cards")
    public ResponseEntity<List<OpportunityCardResponse>> getOpportunityCards(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String format
    ) {
        // Format parametrini normalize edirik
        String normalizedFormat = normalizeFormat(format);

        return ResponseEntity.ok(
                projectService.getAllOpportunitiesForCards(
                        userId,
                        search,
                        category,
                        normalizedFormat
                )
        );
    }

    private String normalizeFormat(String format) {
        if (format == null || format.isBlank()) {
            return null;
        }

        String trimmed = format.trim().toLowerCase();

        // "Hamısı" seçildikdə null qaytarırıq ki, JPQL filtri sıfırlasın
        if (trimmed.equals("hamısı") || trimmed.equals("hamisi") || trimmed.equals("all")) {
            return null;
        }

        // Azerbaycan dilində gələn dəyərləri DB-dəki "Online" / "Offline" ilə üst-üstə salırıq
        if (trimmed.equals("onlayn") || trimmed.equals("online")) {
            return "Online";
        }

        if (trimmed.equals("əyani") || trimmed.equals("eyani") || trimmed.equals("offline")) {
            return "Offline";
        }

        return format;
    }
}