package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.Opportunity;
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

    @Query("SELECT o FROM Opportunity o WHERE " +
            "o.active = true AND " +
            "(:category IS NULL OR o.category = :category) AND " +
            "(:format IS NULL OR LOWER(o.typeDetail) LIKE LOWER(CONCAT('%', :format, '%')) OR " + // <-- LIKE ƏLAVƏ EDİLDİ
            "(:format = 'Onlayn' AND LOWER(o.typeDetail) LIKE '%online%') OR " + // <-- "Onlayn" gələrsə "online" da axtarır
            "(:format = 'Əyani' AND (LOWER(o.typeDetail) LIKE '%offline%' OR LOWER(o.typeDetail) LIKE '%əyani%'))) AND " +
            "(:search IS NULL OR " +
            "LOWER(o.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(o.country) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(o.typeDetail) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Opportunity> searchOpportunities(
            @Param("search") String search,
            @Param("category") String category,
            @Param("format") String format); // <-- @Param("format") ƏLAVƏ EDİLDİ

    List<Opportunity> findByActiveTrue();
    Optional<Opportunity> findByIdAndActiveTrue(Long id);
}
