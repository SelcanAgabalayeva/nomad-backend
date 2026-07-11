package nomad.example.nomad_backend.service;


import nomad.example.nomad_backend.dtos.*;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto request);
    LoginResponseDto login(LoginRequestDto request);

    void logout(RefreshTokenRequestDto request);

    UserResponseDto me();
}