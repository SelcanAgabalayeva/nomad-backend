package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
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
}
