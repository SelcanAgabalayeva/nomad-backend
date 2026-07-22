package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.entity.Like;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.repository.LikeRepository;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import nomad.example.nomad_backend.repository.ProjectRepository;
import nomad.example.nomad_backend.repository.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final OpportunityRepository opportunityRepository;
    private final MessageSource messageSource;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository,
                       ProjectRepository projectRepository, OpportunityRepository opportunityRepository,
                       MessageSource messageSource) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.opportunityRepository = opportunityRepository;
        this.messageSource = messageSource;
    }

    // Wishlist-dəki addToWishlist ilə eyni məntiq: "opportunityId" gəlir,
    // lazım olsa UserProject yaradılır (status SAVED qalır, çünki status
    // "saxlanma" vəziyyətini bildirir, "like" ayrı cədvəldə izlənir),
    // sonra ona bağlı Like sətri qurulur.
    public Like addLike(Integer userId, Long opportunityId) {
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

        return likeRepository.findByUserIdAndProjectId(userId, project.getId())
                .orElseGet(() -> likeRepository.save(new Like(null, user, project, null)));
    }

    public List<UserProject> getUserLikes(Integer userId) {
        return likeRepository.findByUserId(userId)
                .stream()
                .map(Like::getProject)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeLike(Integer userId, Long opportunityId) {
        projectRepository.findByUser_IdAndOpportunity_Id(userId, opportunityId)
                .ifPresent(project ->
                        likeRepository.deleteByUserIdAndProjectId(userId, project.getId())
                );
    }
}