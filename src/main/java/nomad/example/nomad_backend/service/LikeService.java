package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.dtos.LikeResponse;
import nomad.example.nomad_backend.dtos.OpportunityResponse;
import nomad.example.nomad_backend.entity.Like;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.repository.LikeRepository;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import nomad.example.nomad_backend.repository.ProjectRepository;
import nomad.example.nomad_backend.repository.UserRepository;
import nomad.example.nomad_backend.service.impls.DurationTypeService;
import nomad.example.nomad_backend.service.impls.VisaService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final OpportunityRepository opportunityRepository;
    private final MessageSource messageSource;
    private final DurationTypeService durationTypeService;
    private final VisaService visaService;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository,
                       ProjectRepository projectRepository, OpportunityRepository opportunityRepository,
                       MessageSource messageSource, DurationTypeService durationTypeService, VisaService visaService) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.opportunityRepository = opportunityRepository;
        this.messageSource = messageSource;
        this.durationTypeService = durationTypeService;
        this.visaService = visaService;
    }

    // Wishlist-dəki addToWishlist ilə eyni məntiq: "opportunityId" gəlir,
    // lazım olsa UserProject yaradılır (status SAVED qalır, çünki status
    // "saxlanma" vəziyyətini bildirir, "like" ayrı cədvəldə izlənir),
    // sonra ona bağlı Like sətri qurulur.
    public LikeResponse addLike(Long userId, Long opportunityId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage(
                                "project.not.found",
                                null,
                                LocaleContextHolder.getLocale()
                        )
                ));

        List<UserProject> existingProjects =
                projectRepository.findAllByUser_IdAndOpportunity_Id(userId, opportunityId);

        UserProject project;

        if (!existingProjects.isEmpty()) {
            project = existingProjects.get(0);
        } else {
            UserProject newProject = new UserProject();
            newProject.setUser(user);
            newProject.setOpportunity(opportunity);
            newProject.setStatus(ProjectStatus.SAVED);
            project = projectRepository.save(newProject);
        }

        Like like = likeRepository.findByUserIdAndProjectId(userId, project.getId())
                .orElseGet(() ->
                        likeRepository.save(
                                new Like(null, user, project, null)
                        )
                );


        return new LikeResponse(
                like.getId(),
                like.getCreatedAt(),
                OpportunityResponse.builder()
                        .id(opportunity.getId())
                        .title(opportunity.getTitle())
                        .country(opportunity.getCountry())

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

    public List<UserProject> getUserLikes(Long userId) {
        return likeRepository.findByUserId(userId)
                .stream()
                .map(Like::getProject)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeLike(Long userId, Long opportunityId) {

        List<UserProject> projects =
                projectRepository.findAllByUser_IdAndOpportunity_Id(userId, opportunityId);

        if (!projects.isEmpty()) {

            UserProject project = projects.get(0);

            likeRepository.deleteByUserIdAndProjectId(
                    userId,
                    project.getId()
            );
        }
    }
}