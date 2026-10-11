package nomad.example.nomad_backend.service;


import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.OpportunityCardResponse;
import nomad.example.nomad_backend.dtos.OrganizationResponse;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.Organization;
import nomad.example.nomad_backend.entity.OrganizationRating;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import nomad.example.nomad_backend.repository.OrganizationRatingRepository;
import nomad.example.nomad_backend.repository.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationRatingRepository ratingRepository;
    private final OpportunityRepository opportunityRepository;

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
        long activeOpportunityCount =
                opportunityRepository.countByOrganizationIdAndActiveTrue(
                        organization.getId()
                );
        Double rating = null;

        if (reviewCount >= 5) {
            Double average =
                    ratingRepository.getAverageRating(organization.getId());

            if (average != null) {
                rating = Math.round(average * 10.0) / 10.0;
            }
        }

        java.time.LocalDate today = java.time.LocalDate.now();

        // 1. Aktiv və Keçmiş imkanları/layihələri bazadan çəkirik
        List<nomad.example.nomad_backend.entity.Opportunity> activeOpps =
                opportunityRepository.findAllByOrganizationIdAndActiveTrueAndDeadlineGreaterThanEqual(organization.getId(), today);

        List<nomad.example.nomad_backend.entity.Opportunity> pastOpps =
                opportunityRepository.findAllByOrganizationIdAndActiveTrueAndDeadlineLessThan(organization.getId(), today);

        // 2. Card DTO-larına map edirik
        List<nomad.example.nomad_backend.dtos.OpportunityCardResponse> activeCardResponses = activeOpps.stream()
                .map(this::mapToCardResponse)
                .toList();

        List<nomad.example.nomad_backend.dtos.OpportunityCardResponse> pastCardResponses = pastOpps.stream()
                .map(this::mapToCardResponse)
                .toList();

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
                .activeOpportunityCount(activeOpportunityCount)
                .activeOpportunities(activeCardResponses)
                .pastOpportunities(pastCardResponses)
                .galleryImages(organization.getGalleryImages())
                .build();
    }
    public List<OpportunityCardResponse> getPastOpportunities(String slug, Long userId) {
        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Təşkilat tapılmadı"));

        LocalDate today = LocalDate.now();

        // Repository-dən həmin təşkilata aid deadline-ı bugündən kiçik olanları çəkirik
        List<Opportunity> pastOpportunities = opportunityRepository
                .findAllByOrganizationIdAndActiveTrueAndDeadlineLessThan(organization.getId(), today);

        // Bunları DTO-ya (OpportunityCardResponse) çevirib qaytarırıq
        return pastOpportunities.stream()
                .map(opp -> mapToCardResponse(opp))
                .toList();
    }
    private nomad.example.nomad_backend.dtos.OpportunityCardResponse mapToCardResponse(
            nomad.example.nomad_backend.entity.Opportunity opp
    ) {
        java.time.LocalDate today = java.time.LocalDate.now();
        long daysLeft = 0;
        if (opp.getDeadline() != null) {
            daysLeft = java.time.temporal.ChronoUnit.DAYS.between(today, opp.getDeadline());
            if (daysLeft < 0) daysLeft = 0;
        }

        return nomad.example.nomad_backend.dtos.OpportunityCardResponse.builder()
                .id(opp.getId())
                .title(opp.getTitle())
                .country(opp.getCountry())
                .city(opp.getCity())
                .type(opp.getType())
                .category(opp.getCategory())
                .typeDetail(opp.getTypeDetail())
                .deadline(opp.getDeadline())
                .openingDate(opp.getOpeningDate())
                .eventDateRange(opp.getEventDateRange())
                .applyLink(opp.getApplyLink())
                .daysLeft(daysLeft)
                .escOrSalto(opp.getEscOrSalto())
                .volunteeringType(opp.getVolunteeringType())
                .description(opp.getSumAz())
                .duration(opp.getDuration())
                .scope(opp.getScope())
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

    @Transactional
    public OrganizationResponse updateOrganization(
            String slug,
            Organization updatedOrganization
    ) {

        Organization organization = organizationRepository
                .findBySlug(slug)
                .orElseThrow(() ->
                        new RuntimeException("Organization not found")
                );

        organization.setName(updatedOrganization.getName());
        organization.setSlug(updatedOrganization.getSlug());
        organization.setTagline(updatedOrganization.getTagline());
        organization.setDescription(updatedOrganization.getDescription());
        organization.setCategories(updatedOrganization.getCategories());
        organization.setWebsite(updatedOrganization.getWebsite());
        organization.setInstagram(updatedOrganization.getInstagram());
        organization.setFacebook(updatedOrganization.getFacebook());
        organization.setEmail(updatedOrganization.getEmail());
        organization.setLocation(updatedOrganization.getLocation());
        organization.setLogo(updatedOrganization.getLogo());

        Organization saved = organizationRepository.save(organization);

        return toResponses(saved);
    }
    @Transactional
    public void deleteOrganization(String slug) {

        Organization organization = organizationRepository
                .findBySlug(slug)
                .orElseThrow(() ->
                        new RuntimeException("Organization not found")
                );

        organizationRepository.delete(organization);
    }
    // 1. Şəkil siyahısını (list) olduğu kimi yeniləmək / əvəzləmək
    @Transactional
    public void updateGalleryImages(String slug, List<String> imageUrls) {
        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Təşkilat tapılmadı"));

        organization.setGalleryImages(imageUrls != null ? imageUrls : new ArrayList<>());
        organizationRepository.save(organization);
    }

    // 2. Qalereyadan müəyyən bir şəkli silmək
    @Transactional
    public void removeGalleryImage(String slug, String imageUrl) {
        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Təşkilat tapılmadı"));

        if (organization.getGalleryImages() != null) {
            organization.getGalleryImages().remove(imageUrl);
            organizationRepository.save(organization);
        }
    }
    @Transactional
    public void addGalleryImagesBatch(String slug, List<String> imageUrls) {
        Organization organization = organizationRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Təşkilat tapılmadı"));

        if (organization.getGalleryImages() == null) {
            organization.setGalleryImages(new ArrayList<>());
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            organization.getGalleryImages().addAll(imageUrls);
            organizationRepository.save(organization);
        }
    }

}