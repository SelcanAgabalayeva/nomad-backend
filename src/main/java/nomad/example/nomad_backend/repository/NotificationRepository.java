package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    Integer countByUserIdAndIsReadFalse(Long id);

}
