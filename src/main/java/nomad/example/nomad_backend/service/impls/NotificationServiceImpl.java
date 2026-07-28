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


    @Override
    @Transactional
    public void notifyInterestedUsers(Opportunity opportunity) {


        List<Like> likes = likeRepository.findAll();


        for (Like like : likes) {


            User user = like.getUser();


            Opportunity likedOpportunity =
                    like.getProject().getOpportunity();


            int score = 0;


            if (same(
                    likedOpportunity.getCountry(),
                    opportunity.getCountry()
            )) {
                score++;
            }


            if (same(
                    likedOpportunity.getType(),
                    opportunity.getType()
            )) {
                score++;
            }


            if (same(
                    likedOpportunity.getCategory(),
                    opportunity.getCategory()
            )) {
                score++;
            }


            // ən azı 2 uyğunluq varsa
            if (score >= 2) {


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


                notificationRepository.save(notification);



                NotificationSettings settings =
                        notificationSettingsRepository
                                .findByUserId(user.getId())
                                .orElse(null);



                if (settings != null
                        && settings.isEmailNotifications()) {


                    emailService.sendInterestNotification(
                            user.getEmail(),
                            opportunity.getTitle()
                    );
                }
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
}
