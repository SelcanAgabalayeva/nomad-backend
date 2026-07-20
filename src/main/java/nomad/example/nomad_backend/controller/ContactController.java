package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.ContactMessageRequest;
import nomad.example.nomad_backend.service.impls.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ContactController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody ContactMessageRequest request) {

        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Ad sahəsi boş buraxıla bilməz!");
        }

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("E-poçt sahəsi boş buraxıla bilməz!");
        }

        if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Mesaj sahəsi boş buraxıla bilməz!");
        }

        try {
            emailService.sendAndSaveContactMessage(request);

            return ResponseEntity.ok("Mesajınız uğurla qəbul edildi və göndərildi!");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Mesaj göndərilərkən xəta baş verdi: " + e.getMessage());
        }
    }
}