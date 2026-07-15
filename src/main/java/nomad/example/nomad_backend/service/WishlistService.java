package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.Wishlist;
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
    private final MessageSource messageSource;

    public WishlistService(WishlistRepository wishlistRepository, UserRepository userRepository,
                           ProjectRepository projectRepository, MessageSource messageSource) {
        this.wishlistRepository = wishlistRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.messageSource = messageSource;
    }

    public Wishlist addToWishlist(Integer userId, Long projectId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("project.not.found", null, LocaleContextHolder.getLocale())
                ));

        return wishlistRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseGet(() -> wishlistRepository.save(new Wishlist(null, user, project, null)));
    }

    public List<UserProject> getUserWishlist(Integer userId) {
        return wishlistRepository.findByUserId(userId)
                .stream()
                .map(Wishlist::getProject)
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFromWishlist(Integer userId, Long projectId) {
        wishlistRepository.deleteByUserIdAndProjectId(userId, projectId);
    }
}