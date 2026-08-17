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
                .map(opportunity -> OpportunityResponse.builder()
                        .id(opportunity.getId())
                        .title(opportunity.getTitle())
                        .country(opportunity.getCountry())
                        .city(opportunity.getCity())
                        .duration(opportunity.getDuration())

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
                );
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

        return ResponseEntity.ok(
                projectService.getAllOpportunitiesForCards(
                        userId,
                        search,
                        category,
                        format
                )
        );
    }
}