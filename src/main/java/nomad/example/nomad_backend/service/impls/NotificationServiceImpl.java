package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.entity.*;
import nomad.example.nomad_backend.repository.*;
import nomad.example.nomad_backend.service.NotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {


    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final nomad.example.nomad_backend.service.impls.EmailService emailService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;


    @Override
    @Transactional
    public void notifyInterestedUsers(Opportunity opportunity) {

        List<User> users = userRepository.findAll();

        for (User user : users) {

            NotificationSettings settings =
                    notificationSettingsRepository
                            .findByUserId(user.getId())
                            .orElse(null);


            if (settings == null) {
                continue;
            }


            // Yeni imkan bildirişləri bağlıdırsa
            if (!settings.isNewOpportunities()) {
                continue;
            }


            // Ölkə filteri
            if (!settings.getCountries().isEmpty()
                    && !settings.getCountries()
                    .contains(opportunity.getCountry())) {

                continue;
            }


            // Kateqoriya / mövzu filteri
            if (!settings.getCategories().isEmpty()
                    && !settings.getCategories()
                    .contains(opportunity.getCategory())) {

                continue;
            }


            // Format filteri (Online / Offline)
            if (!settings.getFormats().isEmpty()
                    && !settings.getFormats()
                    .contains(opportunity.getTypeDetail())) {

                continue;
            }


            // Layihə növü (ESC, Erasmus+, Internship)
            if (!settings.getProjectTypes().isEmpty()
                    && !settings.getProjectTypes()
                    .contains(opportunity.getType())) {

                continue;
            }



            Notification notification =
                    Notification.builder()
                            .user(user)
                            .title("Yeni imkan")
                            .message(
                                    "Maraq dairənizə uyğun yeni layihə əlavə edildi: "
                                            + opportunity.getTitle()
                            )
                            .isRead(false)
                            .createdAt(LocalDateTime.now())
                            .build();



            // Platforma daxilində bildiriş
            if (settings.isInAppNotifications()) {

                notificationRepository.save(notification);
            }



            // Email bildirişi
            if (settings.isEmailNotifications()) {

                emailService.sendInterestNotification(
                        user.getEmail(),
                        opportunity.getTitle()
                );
            }
        }
    }

    private boolean same(String a, String b) {

        return a != null
                && b != null
                && a.equalsIgnoreCase(b);
    }



    @Override
    public List<Notification> getMyNotifications() {


        User user =
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();


        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(
                        user.getId()
                );
    }



    @Override
    @Transactional
    public void markAsRead(Long notificationId) {


        Notification notification =
                notificationRepository
                        .findById(notificationId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Notification not found"
                                )
                        );


        notification.setRead(true);


        notificationRepository.save(notification);
    }
    @Override
    public Integer getUnreadCount() {

        User user =
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();

        return notificationRepository
                .countByUserIdAndIsReadFalse(user.getId());
    }
    @Override
    @Transactional
    public void notifySavedProjectChanges(Opportunity opportunity) {


        List<UserProject> savedProjects =
                projectRepository.findByOpportunityIdAndStatus(
                        opportunity.getId(),
                        ProjectStatus.SAVED
                );


        for (UserProject project : savedProjects) {


            User user = project.getUser();


            NotificationSettings settings =
                    notificationSettingsRepository
                            .findByUserId(user.getId())
                            .orElse(null);



            if(settings == null){
                continue;
            }



            if(!settings.isSavedProjectChanges()){
                continue;
            }



            Notification notification =
                    Notification.builder()
                            .user(user)
                            .title("Yadda saxlanılan layihə yeniləndi")
                            .message(
                                    opportunity.getTitle()
                                            + " layihəsində dəyişiklik edildi."
                            )
                            .isRead(false)
                            .createdAt(LocalDateTime.now())
                            .build();



            if(settings.isInAppNotifications()){

                notificationRepository.save(notification);
            }



            if(settings.isEmailNotifications()){

                emailService.sendInterestNotification(
                        user.getEmail(),
                        opportunity.getTitle()
                );
            }
        }
    }
}
