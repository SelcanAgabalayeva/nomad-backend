package nomad.example.nomad_backend.service.impls;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.*;
import nomad.example.nomad_backend.entity.RefreshToken;
import nomad.example.nomad_backend.entity.Role;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.exception.ConflictException;
import nomad.example.nomad_backend.exception.ForbiddenException;
import nomad.example.nomad_backend.exception.UnauthorizedException;
import nomad.example.nomad_backend.repository.RefreshTokenRepository;
import nomad.example.nomad_backend.repository.UserRepository;
import nomad.example.nomad_backend.security.JwtService;
import nomad.example.nomad_backend.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Collections;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;


    @Value("${google.client-id}")
    private String googleClientId;


    private void saveRefreshToken(User user, String refreshToken) {
        RefreshToken entity = RefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiresAt(
                        jwtService.extractExpiration(refreshToken)
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                )
                .createdAt(LocalDateTime.now())
                .build();

        refreshTokenRepository.save(entity);
    }

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {

        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email artıq mövcuddur.");
        }

        if(userRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new RuntimeException("Telefon nömrəsi artıq istifadə olunur.");
        }

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new RuntimeException("Şifrələr uyğun gəlmir.");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .university(request.getUniversity())
                .major(request.getMajor())
                .educationLevel(request.getEducationLevel())
                .interests(request.getInterests())
                .termsAccepted(request.isTermsAccepted())
                .newsletter(request.isNewsletter())
                .provider("LOCAL")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        saveRefreshToken(user, refreshToken);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .message("Qeydiyyat uğurla tamamlandı.")
                .build();
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {


        User user =
                userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() ->
                                new RuntimeException("Email və ya şifrə yanlışdır")
                        );


        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )){

            throw new RuntimeException(
                    "Email və ya şifrə yanlışdır"
            );
        }



        String token =
                jwtService.generateToken(user);


        String refreshToken = jwtService.generateRefreshToken(user);
        saveRefreshToken(user, refreshToken);

        return LoginResponseDto.builder()
                .token(token)
                .role(user.getRole().name())
                .refreshToken(refreshToken)
                .build();

    }

    @Override
    public LoginResponseDto loginWithGoogle(GoogleLoginRequestDto request) {

        GoogleIdToken.Payload payload = verifyGoogleToken(request.getIdToken());

        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .firstName(firstName != null ? firstName : "")
                            .lastName(lastName != null ? lastName : "")
                            .email(email)
                            .password(null)
                            .provider("GOOGLE")
                            .role(Role.USER)
                            .termsAccepted(true)
                            .newsletter(false)
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveRefreshToken(user, refreshToken);

        return LoginResponseDto.builder()
                .token(token)
                .role(user.getRole().name())
                .refreshToken(refreshToken)
                .build();
    }

    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new UnauthorizedException("Google token etibarsızdır.");
            }
            return idToken.getPayload();

        } catch (GeneralSecurityException | IOException e) {
            throw new UnauthorizedException("Google token yoxlanılarkən xəta baş verdi.");
        }
    }


    @Transactional
    @Override
    public void logout(
            RefreshTokenRequestDto request
    ) {

        refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .ifPresent(refreshTokenRepository::delete);
    }
    @Override
    public UserResponseDto me() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        User user =
                (User) authentication.getPrincipal();

        return modelMapper.map(
                user,
                UserResponseDto.class
        );
    }

    private String generateRefreshToken() {

        byte[] bytes = new byte[64];

        new SecureRandom().nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}