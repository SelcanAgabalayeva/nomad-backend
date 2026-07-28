package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.NotificationSettingsRequestDto;
import nomad.example.nomad_backend.dtos.NotificationSettingsResponseDto;
import nomad.example.nomad_backend.entity.NotificationSettings;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.NotificationSettingsRepository;
import nomad.example.nomad_backend.service.NotificationSettingsService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationSettingsServiceImpl
        implements NotificationSettingsService {


    private final NotificationSettingsRepository repository;
    private final ModelMapper modelMapper;



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

}