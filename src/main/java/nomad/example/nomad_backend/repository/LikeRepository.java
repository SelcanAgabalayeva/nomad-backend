package nomad.example.nomad_backend.repository;
import nomad.example.nomad_backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserId(Integer userId);
    Optional<Like> findByUserIdAndProjectId(Integer userId, Long projectId);
    void deleteByUserIdAndProjectId(Integer userId, Long projectId);
}