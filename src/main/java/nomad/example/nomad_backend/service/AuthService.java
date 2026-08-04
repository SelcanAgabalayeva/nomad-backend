package nomad.example.nomad_backend.service;


import jakarta.validation.Valid;
import nomad.example.nomad_backend.dtos.*;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto request);
    LoginResponseDto login(LoginRequestDto request);
    LoginResponseDto loginWithGoogle(GoogleLoginRequestDto request);

    void logout(RefreshTokenRequestDto request);

    UserResponseDto me();
    void verifyEmail(String token);

    void resendVerificationEmail(String email);

    AuthResponseDto refreshToken(RefreshTokenRequestDto request);
}