package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.CompleteProfileRequest;
import nomad.example.nomad_backend.dtos.ProfileResponse;
import nomad.example.nomad_backend.dtos.UpdateProfileRequest;
import nomad.example.nomad_backend.dtos.UserResponseDto;
import nomad.example.nomad_backend.entity.NotificationSettings;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.NotificationSettingsRepository;
import nomad.example.nomad_backend.repository.UserRepository;
import nomad.example.nomad_backend.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl
        implements ProfileService {


    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;

    private final NotificationSettingsRepository notificationSettingsRepository;


    @Override
    @Transactional
    public UserResponseDto uploadProfileImage(
            MultipartFile file
    ) throws IOException {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        User user =
                (User) auth.getPrincipal();


        String url = fileStorageService.save(file);


        user.setProfileImageUrl(url);


        userRepository.save(user);


        return modelMapper.map(
                user,
                UserResponseDto.class
        );
    }



    @Override
    @Transactional
    public void deleteProfileImage(){

        User user =
                (User) SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();


        user.setProfileImageUrl(null);


        userRepository.save(user);
    }

    @Override
    public ProfileResponse getProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User tapılmadı"));


        return mapToResponse(user);
    }



    @Override
    public ProfileResponse updateProfile(Long userId,
                                         UpdateProfileRequest request) {


        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User tapılmadı"));


        user.setFirstName(request.getFirstName());

        user.setLastName(request.getLastName());

        user.setPhoneNumber(request.getPhoneNumber());

        user.setCountry(request.getCountry());

        user.setCity(request.getCity());

        user.setUniversity(request.getUniversity());

        user.setMajor(request.getMajor());

        user.setEducationLevel(request.getEducationLevel());

        user.setBirthDate(request.getBirthDate());



        userRepository.save(user);


        return mapToResponse(user);
    }

    @Override
    @Transactional
    public void completeProfile(Long id, CompleteProfileRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Telefon nömrəsi başqa istifadəçiyə məxsusdursa
        if (request.getPhoneNumber() != null &&
                !request.getPhoneNumber().equals(user.getPhoneNumber())) {

            userRepository.findByPhoneNumber(request.getPhoneNumber())
                    .ifPresent(existingUser -> {
                        throw new RuntimeException("Bu telefon nömrəsi artıq istifadə olunur.");
                    });
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBirthDate(request.getBirthDate());
        user.setUniversity(request.getUniversity());
        user.setMajor(request.getMajor());
        user.setEducationLevel(request.getEducationLevel());
        user.setCountry(request.getCountry());
        user.setCity(request.getCity());

        user.setNewsletter(request.isNewsletter());

        if (request.getInterests() != null) {
            user.setInterests(request.getInterests());
        }

        user.setProfileCompleted(true);

        userRepository.save(user);
    }
    @Override
    @Transactional(readOnly = true)
    public NotificationSettings getPreferences(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User tapılmadı"));


        return notificationSettingsRepository
                .findByUser(user)
                .orElseGet(() -> {

                    NotificationSettings settings =
                            NotificationSettings.builder()
                                    .user(user)
                                    .emailNotifications(true)
                                    .inAppNotifications(true)
                                    .newOpportunities(true)
                                    .deadlineReminders(true)
                                    .savedProjectChanges(true)
                                    .platformUpdates(true)
                                    .build();


                    return notificationSettingsRepository.save(settings);
                });
    }
    private ProfileResponse mapToResponse(User user){

        return ProfileResponse.builder()

                .id(user.getId())

                .firstName(user.getFirstName())

                .lastName(user.getLastName())

                .email(user.getEmail())

                .phoneNumber(user.getPhoneNumber())

                .country(user.getCountry())

                .city(user.getCity())

                .university(user.getUniversity())

                .major(user.getMajor())

                .educationLevel(user.getEducationLevel())

                .birthDate(user.getBirthDate())

                .bio(user.getBio())

                .profileImageUrl(user.getProfileImageUrl())

                .profileCompleted(user.isProfileCompleted())

                .build();
    }


}
