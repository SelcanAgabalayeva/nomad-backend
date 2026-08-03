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

import java.util.HashSet;
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
                        .orElseGet(() -> {

                            NotificationSettings newSettings = new NotificationSettings();

                            newSettings.setUser(user);

                            newSettings.setEmailNotifications(true);
                            newSettings.setInAppNotifications(true);
                            newSettings.setNewOpportunities(true);
                            newSettings.setDeadlineReminders(true);
                            newSettings.setSavedProjectChanges(true);
                            newSettings.setPlatformUpdates(true);

                            return repository.save(newSettings);
                        });

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
        settings.setDeadlineReminderDays(
                request.getDeadlineReminderDays()
        );

        settings.setSavedProjectChanges(
                request.isSavedProjectChanges()
        );


        settings.setPlatformUpdates(
                request.isPlatformUpdates()
        );
        settings.setCountries(
                request.getCountries() == null
                        ? new HashSet<>()
                        : request.getCountries()
        );


        settings.setCategories(
                request.getCategories() == null
                        ? new HashSet<>()
                        : request.getCategories()
        );


        settings.setFormats(
                request.getFormats() == null
                        ? new HashSet<>()
                        : request.getFormats()
        );


        settings.setProjectTypes(
                request.getProjectTypes() == null
                        ? new HashSet<>()
                        : request.getProjectTypes()
        );

        settings.setDuration(
                request.getDuration()
        );

        repository.save(settings);


        return modelMapper.map(
                settings,
                NotificationSettingsResponseDto.class
        );

    }

}