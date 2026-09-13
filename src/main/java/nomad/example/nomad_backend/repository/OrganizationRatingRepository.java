package nomad.example.nomad_backend.repository;


import nomad.example.nomad_backend.entity.OrganizationRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrganizationRatingRepository
        extends JpaRepository<OrganizationRating, Long> {

    Optional<OrganizationRating> findByOrganizationIdAndUserId(
            Long organizationId,
            Long userId
    );

    long countByOrganizationId(Long organizationId);

    @Query("""
            SELECT AVG(r.rating)
            FROM OrganizationRating r
            WHERE r.organization.id = :organizationId
            """)
    Double getAverageRating(Long organizationId);
}