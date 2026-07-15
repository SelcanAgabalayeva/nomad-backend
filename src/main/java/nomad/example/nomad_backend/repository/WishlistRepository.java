package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserId(Integer userId);
    Optional<Wishlist> findByUserIdAndProjectId(Integer userId, Long projectId);
    void deleteByUserIdAndProjectId(Integer userId, Long projectId);
}