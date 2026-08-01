package nomad.example.nomad_backend.repository;
import nomad.example.nomad_backend.entity.EmailVerificationToken;
import nomad.example.nomad_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {


    Optional<EmailVerificationToken> findByToken(String token);


    Optional<EmailVerificationToken> findByUserEmail(String email);
    void deleteByUser(User user);
}