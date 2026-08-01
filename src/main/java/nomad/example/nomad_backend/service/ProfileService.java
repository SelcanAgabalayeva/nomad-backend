package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.dtos.ProfileResponse;
import nomad.example.nomad_backend.dtos.UpdateProfileRequest;
import nomad.example.nomad_backend.dtos.UserResponseDto;
import nomad.example.nomad_backend.entity.NotificationSettings;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {

    UserResponseDto uploadProfileImage(
            MultipartFile file
    ) throws IOException;

    void deleteProfileImage();
    ProfileResponse getProfile(Long userId);


    ProfileResponse updateProfile(Long userId, UpdateProfileRequest request);
    void completeProfile(Long id);

    NotificationSettings getPreferences(Long id);
}
