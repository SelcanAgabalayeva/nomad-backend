package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUserId(Long userId);

    Optional<Wishlist> findByUserIdAndProjectId(Long userId, Long projectId);

    void deleteByUserIdAndProjectId(Long userId, Long projectId);

    // Yeni əlavə olunan bildiriş üçün filtrasiya sorğusu:
    @Query("SELECT DISTINCT w.user FROM Wishlist w " +
            "JOIN w.project up " +
            "JOIN up.opportunity o " +
            "WHERE o.country = :country " +
            "OR o.type = :type " +
            "OR o.category = :category")
    List<User> findUsersWithMatchingPreferences(
            @Param("country") String country,
            @Param("type") String type,
            @Param("category") String category
    );

    void deleteByUser(User user);

}