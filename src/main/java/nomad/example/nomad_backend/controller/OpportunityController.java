package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.*;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.OpportunityStatus;
import nomad.example.nomad_backend.enums.OpportunityScope;
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

import java.time.LocalDateTime;
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
    public List<OpportunityResponse> getAll(
            @RequestParam(required = false) String format,
            @RequestParam(required = false) OpportunityScope scope
    ) {

        String normalizedFormat = normalizeFormat(format);

        List<Opportunity> opportunities;

        if (scope != null) {
            opportunities = repository
                    .findAllByActiveTrueAndScopeOrderByDeadlineAsc(scope);
        } else {
            opportunities = repository
                    .findAllByActiveTrueOrderByDeadlineAsc();
        }

        return opportunities
                .stream()
                .filter(o -> {
                    if (normalizedFormat == null) return true;

                    return o.getTypeDetail() != null
                            && o.getTypeDetail()
                            .equalsIgnoreCase(normalizedFormat);
                })
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
                            .typeDetail(typeDetail)
                            .escOrSalto(escOrSalto)
                            .volunteeringType(opportunity.getVolunteeringType())
                            .category(opportunity.getCategory())
                            .applyLink(opportunity.getApplyLink())
                            .scope(opportunity.getScope())
                            .build();
                })
                .toList();
    }
    @PostMapping
    public OpportunityResponse addOpportunity(
            @RequestBody OpportunityRequest request
    ) {

        Opportunity opportunity = Opportunity.builder()
                .title(request.getTitle())
                .deadline(request.getDeadline())
                .type(request.getType())
                .category(request.getCategory())
                .sumAz(request.getSumAz())
                .sumEn(request.getSumEn())
                .sumRus(request.getSumRus())
                .typeDetail(request.getTypeDetail())
                .country(request.getCountry())
                .applyLink(request.getApplyLink())
                .openingDate(request.getOpeningDate())
                .uniqueKey(request.getUniqueKey())
                .duration(request.getDuration())
                .language(request.getLanguage())
                .city(request.getCity())
                .escOrSalto(request.getEscOrSalto())
                .eventDateRange(request.getEventDateRange())
                .financialSupport(request.getFinancialSupport())
                .ageRequirement(request.getAgeRequirement())
                .volunteeringType(request.getVolunteeringType())
                .active(true)
                .createdAt(LocalDateTime.now())
                .status(OpportunityStatus.READY)
                .scope(request.getScope())
                .build();

        Opportunity saved = repository.save(opportunity);

        return OpportunityResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .country(saved.getCountry())
                .city(saved.getCity())
                .duration(saved.getDuration())
                .durationType(
                        durationTypeService.determine(saved.getDuration())
                )
                .visaType(
                        visaService.determine(saved.getCountry())
                )
                .eventDateRange(saved.getEventDateRange())
                .deadline(saved.getDeadline())
                .type(saved.getType())
                .typeDetail(saved.getTypeDetail())
                .escOrSalto(saved.getEscOrSalto())
                .category(saved.getCategory())
                .applyLink(saved.getApplyLink())
                .volunteeringType(saved.getVolunteeringType())
                .scope(saved.getScope())
                .build();
    }

    @PutMapping("/{id}")
    public OpportunityResponse updateOpportunity(
            @PathVariable Long id,
            @RequestBody OpportunityRequest request
    ) {

        Opportunity opportunity = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Opportunity tapılmadı: " + id)
                );

        opportunity.setTitle(request.getTitle());
        opportunity.setDeadline(request.getDeadline());
        opportunity.setType(request.getType());
        opportunity.setCategory(request.getCategory());

        opportunity.setSumAz(request.getSumAz());
        opportunity.setSumEn(request.getSumEn());
        opportunity.setSumRus(request.getSumRus());
        opportunity.setScope(request.getScope());
        opportunity.setTypeDetail(request.getTypeDetail());
        opportunity.setCountry(request.getCountry());
        opportunity.setApplyLink(request.getApplyLink());
        opportunity.setOpeningDate(request.getOpeningDate());
        opportunity.setUniqueKey(request.getUniqueKey());

        opportunity.setDuration(request.getDuration());
        opportunity.setLanguage(request.getLanguage());
        opportunity.setCity(request.getCity());

        opportunity.setEscOrSalto(
                request.getEscOrSalto() != null
                        ? request.getEscOrSalto().trim().toUpperCase()
                        : null
        );

        opportunity.setEventDateRange(request.getEventDateRange());
        opportunity.setFinancialSupport(request.getFinancialSupport());
        opportunity.setAgeRequirement(request.getAgeRequirement());
        opportunity.setVolunteeringType(request.getVolunteeringType());

        Opportunity saved = repository.save(opportunity);

        return OpportunityResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .country(saved.getCountry())
                .city(saved.getCity())
                .duration(saved.getDuration())
                .durationType(
                        durationTypeService.determine(saved.getDuration())
                )
                .visaType(
                        visaService.determine(saved.getCountry())
                )
                .eventDateRange(saved.getEventDateRange())
                .deadline(saved.getDeadline())
                .type(saved.getType())
                .typeDetail(saved.getTypeDetail())
                .escOrSalto(saved.getEscOrSalto())
                .category(saved.getCategory())
                .applyLink(saved.getApplyLink())
                .volunteeringType(saved.getVolunteeringType())
                .scope(saved.getScope())
                .build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOpportunity(@PathVariable Long id) {

        Opportunity opportunity = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Opportunity tapılmadı: " + id)
                );

        repository.delete(opportunity);

        return ResponseEntity.noContent().build();
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
                sorting = Sort.by(Sort.Direction.DESC, "id");
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