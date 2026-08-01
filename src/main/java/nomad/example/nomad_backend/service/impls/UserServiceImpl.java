package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.ChangePasswordRequest;
import nomad.example.nomad_backend.dtos.UpdateUserRequest;
import nomad.example.nomad_backend.dtos.UserResponse;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.*;
import nomad.example.nomad_backend.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final WishlistRepository wishlistRepository;
    private final LikeRepository likeRepository;
    private final ProjectRepository projectRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;



    @Override
    public UserResponse updateProfile(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tapılmadı"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setBirthDate(request.getBirthDate());
        user.setUniversity(request.getUniversity());
        user.setMajor(request.getMajor());
        user.setEducationLevel(request.getEducationLevel());

        if (request.getInterests() != null) {
            user.setInterests(request.getInterests());
        }

        if (request.getNewsletter() != null) {
            user.setNewsletter(request.getNewsletter());
        }

        userRepository.save(user);

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .university(user.getUniversity())
                .major(user.getMajor())
                .educationLevel(user.getEducationLevel())
                .interests(user.getInterests())
                .newsletter(user.isNewsletter())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .emailVerified(user.isEmailVerified())
                .build();
    }
    @Override
    @Transactional
    public void deleteAccount(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tapılmadı"));


        emailVerificationTokenRepository.deleteByUser(user);

        passwordResetTokenRepository.deleteByUser(user);

        refreshTokenRepository.deleteByUser(user);

        wishlistRepository.deleteByUser(user);

        likeRepository.deleteByUser(user);

        projectRepository.deleteByUser(user);

        userRepository.delete(user);
    }
    @Override
    public void changePassword(Long id, ChangePasswordRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tapılmadı"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Cari şifrə yanlışdır");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }
    @Override
    public UserResponse getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User tapılmadı"));

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate())
                .university(user.getUniversity())
                .major(user.getMajor())
                .educationLevel(user.getEducationLevel())
                .interests(user.getInterests())
                .newsletter(user.isNewsletter())
                .profileImageUrl(user.getProfileImageUrl())
                .role(user.getRole())
                .emailVerified(user.isEmailVerified())
                .build();
    }
}
