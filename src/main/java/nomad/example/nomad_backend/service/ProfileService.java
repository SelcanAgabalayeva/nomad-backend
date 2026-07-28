package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.dtos.UserResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {

    UserResponseDto uploadProfileImage(
            MultipartFile file
    ) throws IOException;

    void deleteProfileImage();

}
