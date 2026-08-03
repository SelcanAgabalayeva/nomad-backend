package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.OpportunityCardResponse;
import nomad.example.nomad_backend.dtos.OpportunityDetailResponse;
import nomad.example.nomad_backend.dtos.PlatformStatsResponse;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import nomad.example.nomad_backend.service.ProjectService;
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
    // məlumatını qarışdırır))) - bu, ayrıca diqqət tələb edən mövcud bir
    // məsələdir, bu dəyişikliklə əlaqəli deyiil.
    @GetMapping
    public List<Opportunity> getAll() {

        return repository.findAllByActiveTrueOrderByDeadlineAsc();
    }
    @GetMapping("/paged")
    public Page<Opportunity> getAllPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAllByActiveTrueOrderByDeadlineAsc(pageable);
    }
    @GetMapping("/{id}/details")
    public ResponseEntity<OpportunityDetailResponse> getDetails(
            @PathVariable Long id,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false, defaultValue = "az") String lang) {

        return ResponseEntity.ok(projectService.getOpportunityDetails(id, userId, lang));
    }
    @GetMapping("/stats")
    public ResponseEntity<PlatformStatsResponse> getStats() {
        return ResponseEntity.ok(projectService.getPlatformStatistics());
    }
    @GetMapping("/cards")
    public ResponseEntity<List<OpportunityCardResponse>> getOpportunityCards(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String format) { // <-- 1. FORMAT PARANETRİ ƏLAVƏ EDİLDİ

        // <-- 2. FORMAT PARAMETRİ SERVICE-Ə ÖTÜRÜLDÜ
        return ResponseEntity.ok(projectService.getAllOpportunitiesForCards(userId, search, category, format));
    }
}
