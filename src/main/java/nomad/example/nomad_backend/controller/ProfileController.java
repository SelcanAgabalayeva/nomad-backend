package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.ProfileResponse;
import nomad.example.nomad_backend.dtos.UpdateProfileRequest;
import nomad.example.nomad_backend.entity.NotificationSettings;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.service.ProfileService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {


    private final ProfileService profileService;
    @GetMapping
    public ResponseEntity<ProfileResponse> getProfile(
            @AuthenticationPrincipal User user){

        return ResponseEntity.ok(
                profileService.getProfile(user.getId())
        );
    }

    @PutMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateProfileRequest request){

        return ResponseEntity.ok(
                profileService.updateProfile(user.getId(),request)
        );
    }
    @PostMapping("/complete")
    public ResponseEntity<?> completeProfile(
            @AuthenticationPrincipal User user){

        profileService.completeProfile(user.getId());

        return ResponseEntity.ok(
                "Profile completed"
        );
    }

    @GetMapping("/preferences")
    public NotificationSettings getPreferences(
            @AuthenticationPrincipal User user){

        return profileService.getPreferences(user.getId());
    }
    @PostMapping(
            value = "/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadImage(
            @RequestPart("file") MultipartFile file
    ) {

        try {

            return ResponseEntity.ok(
                    profileService.uploadProfileImage(file)
            );

        } catch (IOException e) {

            return ResponseEntity.internalServerError()
                    .body(
                            Map.of(
                                    "success", false,
                                    "message", "Şəkil yüklənərkən xəta baş verdi"
                            )
                    );
        }
    }


    @DeleteMapping("/image")
    public ResponseEntity<?> deleteImage(){

        profileService.deleteProfileImage();

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "Profile image deleted"
                )
        );
    }

}