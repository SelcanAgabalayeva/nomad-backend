package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.dto.GoogleProfileCompleteRequest;
import nomad.example.nomad_backend.dto.UserRegisterRequest;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerNormalUser(UserRegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("ERROR_EMAIL_ALREADY_EXISTS");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phoneNumber(request.getPhoneNumber())
                .birthDate(request.getBirthDate())
                .university(request.getUniversity())
                .educationLevel(request.getEducationLevel())
                .major(request.getMajor())
                .isSubscribedToEmails(request.getIsSubscribedToEmails() != null ? request.getIsSubscribedToEmails() : false)
                .build();

        return userRepository.save(user);
    }

    public User completeGoogleProfile(GoogleProfileCompleteRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("ERROR_USER_NOT_FOUND"));

        user.setPhoneNumber(request.getPhoneNumber());
        user.setBirthDate(request.getBirthDate());
        user.setUniversity(request.getUniversity());
        user.setEducationLevel(request.getEducationLevel());
        user.setMajor(request.getMajor());

        if (request.getIsSubscribedToEmails() != null) {
            user.setIsSubscribedToEmails(request.getIsSubscribedToEmails());
        }

        return userRepository.save(user);
    }
}