package nomad.example.nomad_backend.repository;


import nomad.example.nomad_backend.entity.NotificationSettings;
import nomad.example.nomad_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationSettingsRepository
        extends JpaRepository<NotificationSettings,Long> {


    Optional<NotificationSettings> findByUserId(Long userId);

    Optional<NotificationSettings> findByUser(User user);

}
