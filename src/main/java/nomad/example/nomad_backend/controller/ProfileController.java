package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {


    private final ProfileService profileService;


    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file
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