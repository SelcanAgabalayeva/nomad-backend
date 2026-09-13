package nomad.example.nomad_backend.service;


import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.OrganizationResponse;
import nomad.example.nomad_backend.entity.Organization;
import nomad.example.nomad_backend.entity.OrganizationRating;
import nomad.example.nomad_backend.repository.OrganizationRatingRepository;
import nomad.example.nomad_backend.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationRatingRepository ratingRepository;

    public List<OrganizationResponse> getAllOrganizations() {

        return organizationRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toResponses)
                .toList();
    }

    public OrganizationResponse getOrganizationBySlug(String slug) {

        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new RuntimeException("Təşkilat tapılmadı")
                );

        return toResponses(organization);
    }



    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }
    private OrganizationResponse toResponses(Organization organization) {

        long reviewCount =
                ratingRepository.countByOrganizationId(organization.getId());

        Double rating = null;

        if (reviewCount >= 5) {
            Double average =
                    ratingRepository.getAverageRating(organization.getId());

            if (average != null) {
                rating = Math.round(average * 10.0) / 10.0;
            }
        }

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
                .rating(rating)
                .reviewCount(reviewCount)
                .build();
    }

    @Transactional
    public void rateOrganization(
            String slug,
            Long userId,
            Double rating
    ) {
        if (rating == null || rating < 0 || rating > 10) {
            throw new IllegalArgumentException(
                    "Rating must be between 0 and 10"
            );
        }

        Organization organization = organizationRepository
                .findBySlug(slug)
                .orElseThrow(() ->
                        new RuntimeException("Organization not found")
                );

        OrganizationRating organizationRating =
                ratingRepository
                        .findByOrganizationIdAndUserId(
                                organization.getId(),
                                userId
                        )
                        .orElse(
                                OrganizationRating.builder()
                                        .organization(organization)
                                        .userId(userId)
                                        .build()
                        );

        organizationRating.setRating(rating);

        ratingRepository.save(organizationRating);
    }
}