package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.NotificationSettingsRequestDto;
import nomad.example.nomad_backend.service.NotificationSettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification-settings")
@RequiredArgsConstructor
public class NotificationSettingsController {


    private final NotificationSettingsService service;



    @GetMapping
    public ResponseEntity<?> getSettings(){

        return ResponseEntity.ok(
                service.getSettings()
        );
    }



    @PutMapping
    public ResponseEntity<?> update(
            @RequestBody NotificationSettingsRequestDto request
    ){

        return ResponseEntity.ok(
                service.update(request)
        );
    }


}
