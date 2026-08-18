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
import org.springframework.data.domain.Sort;
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
    public List<OpportunityResponse> getAll(@RequestParam(required = false) String format) {

        String normalizedFormat = normalizeFormat(format);

        return repository.findAllByActiveTrueOrderByDeadlineAsc()
                .stream()
                .filter(o -> {
                    if (normalizedFormat == null) return true;
                    return o.getTypeDetail() != null && o.getTypeDetail().equalsIgnoreCase(normalizedFormat);
                })
                .map(opportunity -> {

                    // 1. typeDetail üçün FRONTEND HİYLƏSİ (null əvəzinə "" göndəririk)
                    String typeDetail = opportunity.getTypeDetail();
                    if (typeDetail == null || "Hamısı".equalsIgnoreCase(typeDetail) || "Hamisi".equalsIgnoreCase(typeDetail)) {
                        typeDetail = "";
                    }

                    // 2. ESC/SALTO boyuk harfle
                    String escOrSalto = opportunity.getEscOrSalto() != null
                            ? opportunity.getEscOrSalto().trim().toUpperCase()
                            : null;

                    return OpportunityResponse.builder()
                            .id(opportunity.getId())
                            .title(opportunity.getTitle())
                            .country(opportunity.getCountry())
                            .city(opportunity.getCity())
                            .duration(opportunity.getDuration())
                            .eventDateRange(opportunity.getEventDateRange())
                            .durationType(durationTypeService.determine(opportunity.getDuration()))
                            .visaType(visaService.determine(opportunity.getCountry()))
                            .deadline(opportunity.getDeadline())
                            .type(opportunity.getType())
                            .typeDetail(typeDetail) // Sahə əlavə olundu
                            .escOrSalto(escOrSalto) // Sahə əlavə olundu
                            .volunteeringType(opportunity.getVolunteeringType()) // Sahə əlavə olundu
                            .category(opportunity.getCategory())
                            .applyLink(opportunity.getApplyLink())
                            .build();
                })
                .toList();
    }
    @GetMapping("/paged")
    public Page<OpportunityResponse> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String format,
            @RequestParam(defaultValue = "deadline") String sort
    ) {

        // Frontend-dən gələn "Onlayn"/"Əyani" sözünü
        // "Online"/"Offline"-a çeviririk
        String normalizedFormat = normalizeFormat(format);

        Sort sorting;

        switch (sort.toLowerCase()) {

            case "newest":
                sorting = Sort.by(Sort.Direction.DESC, "createdAt");
                break;

            case "city":
                sorting = Sort.by(Sort.Direction.ASC, "city");
                break;

            case "deadline":
            default:
                sorting = Sort.by(Sort.Direction.ASC, "deadline");
                break;
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        // Bazadan filter + pagination + sorting ilə datanı çəkirik
        return repository
                .findAllByFormat(normalizedFormat, pageable)
                .map(opportunity -> {

                    String typeDetail = opportunity.getTypeDetail();

                    if (typeDetail == null
                            || "Hamısı".equalsIgnoreCase(typeDetail)
                            || "Hamisi".equalsIgnoreCase(typeDetail)) {
                        typeDetail = "";
                    }

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
                            .durationType(
                                    durationTypeService.determine(opportunity.getDuration())
                            )
                            .visaType(
                                    visaService.determine(opportunity.getCountry())
                            )
                            .deadline(opportunity.getDeadline())
                            .type(opportunity.getType())
                            .typeDetail(typeDetail)
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