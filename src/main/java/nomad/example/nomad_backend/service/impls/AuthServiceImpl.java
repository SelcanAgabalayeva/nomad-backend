package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.*;
import nomad.example.nomad_backend.entity.RefreshToken;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.exception.ConflictException;
import nomad.example.nomad_backend.exception.ForbiddenException;
import nomad.example.nomad_backend.exception.UnauthorizedException;
import nomad.example.nomad_backend.repository.RefreshTokenRepository;
import nomad.example.nomad_backend.repository.UserRepository;
import nomad.example.nomad_backend.security.JwtService;
import nomad.example.nomad_backend.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

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
                .build();

        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

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


        return new LoginResponseDto(token);

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