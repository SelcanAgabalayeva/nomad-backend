package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.Wishlist;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import nomad.example.nomad_backend.repository.ProjectRepository;
import nomad.example.nomad_backend.repository.UserRepository;
import nomad.example.nomad_backend.repository.WishlistRepository;
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

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository,
                           ProjectRepository projectRepository, OpportunityRepository opportunityRepository,
                           MessageSource messageSource) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.opportunityRepository = opportunityRepository;
        this.messageSource = messageSource;
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
    public Wishlist addToWishlist(Long userId, Long opportunityId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Opportunity opportunity = opportunityRepository.findById(opportunityId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("project.not.found", null, LocaleContextHolder.getLocale())
                ));

        UserProject project = projectRepository.findByUser_IdAndOpportunity_Id(userId, opportunityId)
                .orElseGet(() -> {
                    UserProject newProject = new UserProject();
                    newProject.setUser(user);
                    newProject.setOpportunity(opportunity);
                    newProject.setStatus(ProjectStatus.SAVED);
                    return projectRepository.save(newProject);
                });

        return wishlistRepository.findByUserIdAndProjectId(userId, project.getId())
                .orElseGet(() -> wishlistRepository.save(new Wishlist(null, user, project, null)));
    }

    public List<UserProject> getUserWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .stream()
                .map(Wishlist::getProject)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long opportunityId) {
        projectRepository.findByUser_IdAndOpportunity_Id(userId, opportunityId)
                .ifPresent(project ->
                        wishlistRepository.deleteByUserIdAndProjectId(userId, project.getId())
                );
    }
}