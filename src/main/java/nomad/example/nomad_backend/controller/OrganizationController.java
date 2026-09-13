package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.OrganizationResponse;
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

    @GetMapping("/{slug}")
    public ResponseEntity<OrganizationResponse> getOrganizationBySlug(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                organizationService.getOrganizationBySlug(slug)
        );
    }
}
