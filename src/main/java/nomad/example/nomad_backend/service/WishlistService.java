package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.dtos.OpportunityResponse;
import nomad.example.nomad_backend.dtos.WishlistResponse;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.Wishlist;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import nomad.example.nomad_backend.repository.ProjectRepository;
import nomad.example.nomad_backend.repository.UserRepository;
import nomad.example.nomad_backend.repository.WishlistRepository;
import nomad.example.nomad_backend.service.impls.DurationTypeService;
import nomad.example.nomad_backend.service.impls.VisaService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final OpportunityRepository opportunityRepository;
    private final MessageSource messageSource;
    private final DurationTypeService durationTypeService;
    private final VisaService visaService;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository,
                           ProjectRepository projectRepository, OpportunityRepository opportunityRepository,
                           MessageSource messageSource, DurationTypeService durationTypeService, VisaService visaService) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.opportunityRepository = opportunityRepository;
        this.messageSource = messageSource;
        this.durationTypeService = durationTypeService;
        this.visaService = visaService;
    }

    // DÜZƏLDİLDİ: "projectId" parametri əslində frontend-dən gələn
    // OPPORTUNITY id-sidir (kartın öz id-si), UserProject-in id-si deyil.
    // Əvvəlki versiya bunu birbaşa UserProject id-si kimi axtarırdı, ona
    // görə "project.not.found" xətası verirdi - çünki istifadəçi üçün
    // hələ heç bir UserProject sətri yox idi.
    //
    // İNDİ: əvvəlcə (userId, opportunityId) cütü ilə mövcud UserProject
    // axtarılır; tapılmasa, YENİ yaradılır (status: SAVED). Sonra bu
    // sətrin ÖZ id-si ilə wishlist-ə bağlanır.
    public WishlistResponse addToWishlist(Long userId, Long opportunityId) {

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


        Wishlist wishlist = wishlistRepository
                .findByUserIdAndProjectId(userId, project.getId())
                .orElseGet(() ->
                        wishlistRepository.save(
                                new Wishlist(null, user, project, null)
                        )
                );


        return new WishlistResponse(
                wishlist.getId(),
                wishlist.getCreatedAt(),

                OpportunityResponse.builder()
                        .id(opportunity.getId())
                        .title(opportunity.getTitle())
                        .country(opportunity.getCountry())

                        .city(opportunity.getCity())
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
    public List<UserProject> getUserWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .stream()
                .map(Wishlist::getProject)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long opportunityId) {

        List<UserProject> projects =
                projectRepository.findAllByUser_IdAndOpportunity_Id(userId, opportunityId);

        if (!projects.isEmpty()) {
            UserProject project = projects.get(0);

            wishlistRepository.deleteByUserIdAndProjectId(
                    userId,
                    project.getId()
            );
        }
    }
}