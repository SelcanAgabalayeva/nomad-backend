package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.OpportunityCardResponse;
import nomad.example.nomad_backend.dtos.OpportunityDetailResponse;
import nomad.example.nomad_backend.dtos.PlatformStatsResponse;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import nomad.example.nomad_backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityRepository repository;
    private final ProjectService projectService;
    // məlumatını qarışdırır) - bu, ayrıca diqqət tələb edən mövcud bir
    // məsələdir, bu dəyişikliklə əlaqəli deyil.
    @GetMapping
    public List<Opportunity> getAll() {
        return repository.findAll();
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
            @RequestParam(required = false) String category) {

        return ResponseEntity.ok(projectService.getAllOpportunitiesForCards(userId, search, category));
    }
}
