package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.ChangePasswordRequest;
import nomad.example.nomad_backend.dtos.UpdateUserRequest;
import nomad.example.nomad_backend.dtos.UserResponse;
import nomad.example.nomad_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import nomad.example.nomad_backend.entity.User;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal User user) {

        UserResponse response = userService.getUser(user.getId());

        return ResponseEntity.ok(response);
    }
    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @RequestBody UpdateUserRequest request) {

        UserResponse response = userService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody ChangePasswordRequest request) {

        userService.changePassword(user.getId(), request);
        return ResponseEntity.ok("Şifrə uğurla dəyişdirildi.");
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteAccount(
            @AuthenticationPrincipal User user) {

        userService.deleteAccount(user.getId());
        return ResponseEntity.ok("Hesab uğurla silindi.");
    }
}