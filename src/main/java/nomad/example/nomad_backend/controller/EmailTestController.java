package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.service.impls.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test-email")
@RequiredArgsConstructor
public class EmailTestController {

    private final EmailService emailService;


    @PostMapping
    public ResponseEntity<String> sendEmail(
            @RequestParam String email,
            @RequestParam String title
    ) {

        emailService.sendDeadlineReminder(email, title);

        return ResponseEntity.ok("Email sent");
    }
}
