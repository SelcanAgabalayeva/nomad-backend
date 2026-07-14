package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.dtos.OpportunityCardResponse;
import nomad.example.nomad_backend.dtos.OpportunityDetailResponse;
import nomad.example.nomad_backend.dtos.PlatformStatsResponse;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import nomad.example.nomad_backend.repository.ProjectRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MessageSource messageSource;
    private final OpportunityRepository opportunityRepository;


    public ProjectService(ProjectRepository projectRepository, MessageSource messageSource, OpportunityRepository opportunityRepository) {
        this.projectRepository = projectRepository;
        this.messageSource = messageSource;
        this.opportunityRepository = opportunityRepository;
    }

    public List<UserProject> getSavedProjects() {
        return projectRepository.findByStatus(ProjectStatus.SAVED);
    }

    public List<UserProject> getAppliedProjects() {
        return projectRepository.findByStatus(ProjectStatus.APPLIED);
    }

    public List<UserProject> getRecommendedProjects(List<String> categories) {
        return projectRepository.findByOpportunity_CategoryIn(categories);
    }

    public UserProject updateProjectStatus(Long projectId, ProjectStatus newStatus) {
        UserProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("project.not.found", null, LocaleContextHolder.getLocale())
                ));

        project.setStatus(newStatus);
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }
    public List<OpportunityCardResponse> getAllOpportunitiesForCards(Long userId) {

        List<Opportunity> opportunities = opportunityRepository.findAll();

        Map<Long, ProjectStatus> userProjectStatusMap = java.util.Collections.emptyMap();
        if (userId != null) {
            userProjectStatusMap = projectRepository.findByUserId(userId).stream()
                    .collect(Collectors.toMap(
                            up -> up.getOpportunity().getId(),
                            UserProject::getStatus,
                            (existing, replacement) -> existing // Əgər eyni ID ilə birdən çox status varsa birini saxla
                    ));
        }

        Map<Long, ProjectStatus> finalMap = userProjectStatusMap;

        return opportunities.stream().map(opp -> {
            // Son tarixə qalan gün sayını hesabla
            long daysLeft = 0;
            if (opp.getDeadline() != null) {
                daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), opp.getDeadline());
                if (daysLeft < 0) daysLeft = 0;
            }

            ProjectStatus status = finalMap.get(opp.getId());
            boolean isSaved = status == ProjectStatus.SAVED;
            boolean isApplied = status == ProjectStatus.APPLIED;

            return OpportunityCardResponse.builder()
                    .id(opp.getId())
                    .title(opp.getTitle())
                    .country(opp.getCountry())
                    .type(opp.getType())
                    .category(opp.getCategory())
                    .sort(opp.getSort())
                    .deadline(opp.getDeadline())
                    .openingDate(opp.getOpeningDate())
                    .daysLeft(daysLeft)
                    .isSaved(isSaved)
                    .isApplied(isApplied)
                    .build();
        }).collect(Collectors.toList());
    }
    public OpportunityDetailResponse getOpportunityDetails(Long opportunityId, Long userId, String lang) {

        Opportunity opp = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException("Elan tapılmadı"));

        boolean isSaved = false;
        boolean isApplied = false;

        if (userId != null) {
            Optional<UserProject> userProjectOpt = projectRepository.findByUserId(userId).stream()
                    .filter(up -> up.getOpportunity().getId().equals(opportunityId))
                    .findFirst();

            if (userProjectOpt.isPresent()) {
                ProjectStatus status = userProjectOpt.get().getStatus();
                isSaved = (status == ProjectStatus.SAVED || status == ProjectStatus.FAVORITE);
                isApplied = (status == ProjectStatus.APPLIED);
            }
        }

        String description = opp.getSumAz();
        if ("en".equalsIgnoreCase(lang) && opp.getSumEn() != null) {
            description = opp.getSumEn();
        } else if ("ru".equalsIgnoreCase(lang) && opp.getSumRus() != null) {
            description = opp.getSumRus();
        }

        return OpportunityDetailResponse.builder()
                .id(opp.getId())
                .title(opp.getTitle())
                .country(opp.getCountry())
                .type(opp.getType())
                .category(opp.getCategory())
                .sort(opp.getSort())
                .deadline(opp.getDeadline())
                .openingDate(opp.getOpeningDate())
                .description(description)
                .applyLink(opp.getApplyLink())
                .isSaved(isSaved)
                .isApplied(isApplied)
                .build();
    }
    public PlatformStatsResponse getPlatformStatistics() {
        long activeCount = opportunityRepository.countByDeadlineGreaterThanEqual(LocalDate.now());
        long categoriesCount = opportunityRepository.countDistinctCategories();

        return PlatformStatsResponse.builder()
                .activeOpportunities(activeCount + "+")
                .servicesCount("10+")
                .availability("7/24")
                .categoriesCount(categoriesCount)
                .build();
    }

    public List<OpportunityCardResponse> getAllOpportunitiesForCards(Long userId, String search, String category) {

        String searchParam = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        String categoryParam = (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("Bütün kateqoriyalar")) ? category.trim() : null;

        List<Opportunity> opportunities = opportunityRepository.searchOpportunities(searchParam, categoryParam);

        Map<Long, ProjectStatus> userProjectStatusMap = java.util.Collections.emptyMap();
        if (userId != null) {
            userProjectStatusMap = projectRepository.findByUserId(userId).stream()
                    .collect(Collectors.toMap(
                            up -> up.getOpportunity().getId(),
                            UserProject::getStatus,
                            (existing, replacement) -> existing
                    ));
        }

        Map<Long, ProjectStatus> finalMap = userProjectStatusMap;

        return opportunities.stream().map(opp -> {
            long daysLeft = 0;
            if (opp.getDeadline() != null) {
                daysLeft = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), opp.getDeadline());
                if (daysLeft < 0) daysLeft = 0;
            }

            ProjectStatus status = finalMap.get(opp.getId());
            boolean isSaved = status == ProjectStatus.SAVED;
            boolean isApplied = status == ProjectStatus.APPLIED;

            return OpportunityCardResponse.builder()
                    .id(opp.getId())
                    .title(opp.getTitle())
                    .country(opp.getCountry())
                    .type(opp.getType())
                    .category(opp.getCategory())
                    .sort(opp.getSort())
                    .deadline(opp.getDeadline())
                    .openingDate(opp.getOpeningDate())
                    .daysLeft(daysLeft)
                    .isSaved(isSaved)
                    .isApplied(isApplied)
                    .build();
        }).collect(Collectors.toList());
    }
}