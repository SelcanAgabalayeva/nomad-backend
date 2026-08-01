package nomad.example.nomad_backend.repository;


import nomad.example.nomad_backend.entity.PasswordResetToken;
import nomad.example.nomad_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Integer> {

    void deleteByUser(User user);
}
