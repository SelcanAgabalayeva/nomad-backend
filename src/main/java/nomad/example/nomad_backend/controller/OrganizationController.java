package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.OpportunityCardResponse;
import nomad.example.nomad_backend.dtos.OrganizationRatingRequest;
import nomad.example.nomad_backend.dtos.OrganizationResponse;
import nomad.example.nomad_backend.entity.Organization;
import nomad.example.nomad_backend.service.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<OrganizationResponse>> getAllOrganizations() {

        return ResponseEntity.ok(
                organizationService.getAllOrganizations()
        );
    }
    @PostMapping
    public ResponseEntity<OrganizationResponse> createOrganization(
            @RequestBody Organization organization
    ) {
        Organization saved = organizationService.save(organization);

        return ResponseEntity.ok(
                organizationService.getOrganizationBySlug(saved.getSlug())
        );
    }
    @GetMapping("/{slug}")
    public ResponseEntity<OrganizationResponse> getOrganizationBySlug(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                organizationService.getOrganizationBySlug(slug)
        );
    }
    @PostMapping("/{slug}/rating")
    public ResponseEntity<Void> rateOrganization(
            @PathVariable String slug,
            @RequestBody OrganizationRatingRequest request
    ) {

        organizationService.rateOrganization(
                slug,
                request.getUserId(),
                request.getRating()
        );

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{slug}")
    public ResponseEntity<OrganizationResponse> updateOrganization(
            @PathVariable String slug,
            @RequestBody Organization organization
    ) {

        return ResponseEntity.ok(
                organizationService.updateOrganization(slug, organization)
        );
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteOrganization(
            @PathVariable String slug
    ) {

        organizationService.deleteOrganization(slug);

        return ResponseEntity.noContent().build();
    }

    // Bütün qalereya şəkillərini massiv (List) şəklində yeniləmək
    @PutMapping("/{slug}/gallery")
    public ResponseEntity<Void> updateGalleryImages(
            @PathVariable String slug,
            @RequestBody List<String> imageUrls
    ) {
        organizationService.updateGalleryImages(slug, imageUrls);
        return ResponseEntity.ok().build();
    }

    // Qalereyadan tək bir şəkli silmək
    @DeleteMapping("/{slug}/gallery")
    public ResponseEntity<Void> removeGalleryImage(
            @PathVariable String slug,
            @RequestParam String imageUrl
    ) {
        organizationService.removeGalleryImage(slug, imageUrl);
        return ResponseEntity.noContent().build();
    }
    // POST metodu ilə çoxlu şəkil (List) əlavə etmək
    @PostMapping("/{slug}/gallery/batch")
    public ResponseEntity<Void> addGalleryImagesBatch(
            @PathVariable String slug,
            @RequestBody List<String> imageUrls
    ) {
        organizationService.addGalleryImagesBatch(slug, imageUrls);
        return ResponseEntity.ok().build();
    }
    // Təşkilatın deadline-ı bitmiş (keçmiş) layihələrini gətirmək üçün
    @GetMapping("/{slug}/past-opportunities")
    public ResponseEntity<List<OpportunityCardResponse>> getPastOpportunities(
            @PathVariable String slug,
            @RequestParam(required = false) Long userId
    ) {
        return ResponseEntity.ok(
                organizationService.getPastOpportunities(slug, userId)
        );
    }
}
