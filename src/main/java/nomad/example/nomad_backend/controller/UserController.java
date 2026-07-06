package nomad.example.nomad_backend.controller;

import jakarta.validation.Valid;
import nomad.example.nomad_backend.dto.GoogleProfileCompleteRequest;
import nomad.example.nomad_backend.dto.UserRegisterRequest;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        User registeredUser = userService.registerNormalUser(request);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/complete-google-profile")
    public ResponseEntity<User> completeGoogleProfile(@Valid @RequestBody GoogleProfileCompleteRequest request) {
        User updatedUser = userService.completeGoogleProfile(request);
        return ResponseEntity.ok(updatedUser);
    }
}