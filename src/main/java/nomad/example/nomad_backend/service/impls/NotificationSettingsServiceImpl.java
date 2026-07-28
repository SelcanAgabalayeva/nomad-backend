package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.NotificationSettingsRequestDto;
import nomad.example.nomad_backend.dtos.NotificationSettingsResponseDto;
import nomad.example.nomad_backend.entity.*;
import nomad.example.nomad_backend.repository.LikeRepository;
import nomad.example.nomad_backend.repository.NotificationRepository;
import nomad.example.nomad_backend.repository.NotificationSettingsRepository;
import nomad.example.nomad_backend.service.NotificationSettingsService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSettingsServiceImpl implements NotificationSettingsService {


    private final NotificationSettingsRepository repository;
    private final ModelMapper modelMapper;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;
    private final EmailService emailService;


    @Override
    public NotificationSettingsResponseDto getSettings(){


        User user =
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();



        NotificationSettings settings =
                repository.findByUserId(user.getId())
                        .orElseThrow();


        return modelMapper.map(
                settings,
                NotificationSettingsResponseDto.class
        );

    }



    @Override
    @Transactional
    public NotificationSettingsResponseDto update(
            NotificationSettingsRequestDto request){


        User user =
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();



        NotificationSettings settings =
                repository.findByUserId(user.getId())
                        .orElseThrow();



        settings.setEmailNotifications(
                request.isEmailNotifications()
        );


        settings.setInAppNotifications(
                request.isInAppNotifications()
        );


        settings.setNewOpportunities(
                request.isNewOpportunities()
        );


        settings.setDeadlineReminders(
                request.isDeadlineReminders()
        );


        settings.setSavedProjectChanges(
                request.isSavedProjectChanges()
        );


        settings.setPlatformUpdates(
                request.isPlatformUpdates()
        );


        repository.save(settings);


        return modelMapper.map(
                settings,
                NotificationSettingsResponseDto.class
        );

    }

    @Override
    @Transactional
    public void notifyInterestedUsers(Opportunity opportunity) {

        List<Like> likes = likeRepository.findAll();

        for (Like like : likes) {

            User user = like.getUser();

            Opportunity likedOpportunity =
                    like.getProject().getOpportunity();

            int score = 0;

            if (likedOpportunity.getCountry() != null
                    && opportunity.getCountry() != null
                    && likedOpportunity.getCountry().equalsIgnoreCase(opportunity.getCountry())) {
                score++;
            }

            if (likedOpportunity.getType() != null
                    && opportunity.getType() != null
                    && likedOpportunity.getType().equalsIgnoreCase(opportunity.getType())) {
                score++;
            }

            if (likedOpportunity.getCategory() != null
                    && opportunity.getCategory() != null
                    && likedOpportunity.getCategory().equalsIgnoreCase(opportunity.getCategory())) {
                score++;
            }

            // Ən azı 2 uyğunluq varsa
            if (score >= 2) {

                NotificationSettings settings =
                        notificationSettingsRepository
                                .findByUserId(user.getId())
                                .orElse(null);

                notificationRepository.save(
                        Notification.builder()
                                .user(user)
                                .title("Yeni imkan")
                                .message("Maraq dairənizə uyğun \"" +
                                        opportunity.getTitle() +
                                        "\" layihəsi əlavə olundu.")
                                .isRead(false)
                                .build()
                );

                if (settings != null && settings.isEmailNotifications()) {

                    emailService.sendInterestNotification(
                            user.getEmail(),
                            opportunity.getTitle()
                    );
                }
            }
        }
    }

}