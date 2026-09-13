package nomad.example.nomad_backend.service;


import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.OrganizationResponse;
import nomad.example.nomad_backend.entity.Organization;
import nomad.example.nomad_backend.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public List<OrganizationResponse> getAllOrganizations() {

        return organizationRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public OrganizationResponse getOrganizationBySlug(String slug) {

        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new RuntimeException("Təşkilat tapılmadı")
                );

        return toResponse(organization);
    }

    private OrganizationResponse toResponse(Organization organization) {

        return OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .slug(organization.getSlug())
                .tagline(organization.getTagline())
                .description(organization.getDescription())
                .categories(organization.getCategories())
                .website(organization.getWebsite())
                .instagram(organization.getInstagram())
                .facebook(organization.getFacebook())
                .email(organization.getEmail())
                .location(organization.getLocation())
                .logo(organization.getLogo())
                .build();
    }
}