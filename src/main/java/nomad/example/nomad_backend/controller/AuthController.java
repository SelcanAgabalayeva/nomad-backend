package nomad.example.nomad_backend.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.*;
import nomad.example.nomad_backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto request){

        return ResponseEntity.ok(authService.register(request));

    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(
            @Valid @RequestBody RefreshTokenRequestDto request
    ) {

        authService.logout(request);

        return ResponseEntity.ok(
                LogoutResponseDto.builder()
                        .success(true)
                        .message("Logged out successfully")
                        .build()
        );
    }
    @GetMapping("/me")
    public ResponseEntity<?> me(){

        Map<String, Object> response = new HashMap<>();

        response.put("success", true);
        response.put("user", authService.me());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @RequestBody LoginRequestDto request
    ){

        return ResponseEntity.ok(
                authService.login(request)
        );

    }
}

