package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.enums.OpportunityScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    long countByActiveTrueAndDeadlineGreaterThanEqual(LocalDate date);

    @Query("SELECT COUNT(DISTINCT o.category) FROM Opportunity o")
    long countDistinctCategories();
    Optional<Opportunity> findByUniqueKey(String uniqueKey);

    @Query("""
SELECT o FROM Opportunity o
WHERE o.active = true
AND (:category IS NULL OR o.category = :category)
AND (
    :format IS NULL
    OR LOWER(TRIM(o.typeDetail)) = LOWER(TRIM(:format))
)
AND (
    :search IS NULL
    OR LOWER(o.title) LIKE CONCAT('%', LOWER(CAST(:search AS string)), '%')
    OR LOWER(o.country) LIKE CONCAT('%', LOWER(CAST(:search AS string)), '%')
    OR LOWER(o.typeDetail) LIKE CONCAT('%', LOWER(CAST(:search AS string)), '%')
)
ORDER BY 
CASE WHEN o.deadline IS NULL THEN 1 ELSE 0 END,
o.deadline ASC
""")
    List<Opportunity> searchOpportunities(
            @Param("search") String search,
            @Param("category") String category,
            @Param("format") String format
    );

    @Query("""
    SELECT o FROM Opportunity o
    WHERE o.active = true
    AND (
        :format IS NULL
        OR o.typeDetail = :format
    )
""")
    Page<Opportunity> findAllByFormat(
            @Param("format") String format,
            Pageable pageable
    );
    List<Opportunity> findByActiveTrue();
    Optional<Opportunity> findByIdAndActiveTrue(Long id);
    List<Opportunity> findAllByActiveTrueOrderByDeadlineAsc();
    Page<Opportunity> findAllByActiveTrueOrderByDeadlineAsc(Pageable pageable);
    List<Opportunity> findAllByActiveTrueAndScopeOrderByDeadlineAsc(
            OpportunityScope scope
    );
    void deleteByDeadlineBefore(LocalDate date);
}
