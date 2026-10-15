package nomad.example.nomad_backend.repository;
import nomad.example.nomad_backend.dtos.MundusProgramListDTO;
import nomad.example.nomad_backend.entity.MundusProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MundusProgramRepository
        extends JpaRepository<MundusProgram, Long> {

    @Query("""
    SELECT new nomad.example.nomad_backend.dtos.MundusProgramListDTO(
        p.id,
        p.programName,
        p.category,
        p.fieldOfStudy,
        p.degree,
        p.deadline,
        p.applicationOpens,
        p.scholarship,
        p.applyLink,
        p.scholarshipAmount
    )
    FROM MundusProgram p
    WHERE p.active = true
    ORDER BY p.deadline ASC
""")
    Page<MundusProgramListDTO> findActiveList(Pageable pageable);

    @Query("""
        SELECT new nomad.example.nomad_backend.dtos.MundusProgramListDTO(
            p.id,
            p.programName,
            p.category,
            p.fieldOfStudy,
            p.degree,
            p.deadline,
            p.applicationOpens,
            p.scholarship,
            p.applyLink,
            p.scholarshipAmount
        )
        FROM MundusProgram p
        ORDER BY p.createdAt DESC
    """)
    Page<MundusProgramListDTO> findAllList(Pageable pageable);
    List<MundusProgram> findAllByActiveTrueOrderByDeadlineAsc();

    List<MundusProgram> findAllByOrderByCreatedAtDesc();

    @Modifying
    @Query("""
    UPDATE MundusProgram p
    SET p.viewCount = COALESCE(p.viewCount, 0) + 1
    WHERE p.id = :id AND p.active = true
""")
    int incrementViewCount(@Param("id") Long id);

    @Modifying
    @Query("""
    UPDATE MundusProgram p
    SET p.applicationClickCount =
        COALESCE(p.applicationClickCount, 0) + 1
    WHERE p.id = :id AND p.active = true
""")
    int incrementApplicationClickCount(@Param("id") Long id);
    long countByActiveTrue();
    @Query("""
    SELECT p.id, c
    FROM MundusProgram p
    JOIN p.countries c
    WHERE p.id IN :ids
""")
    List<Object[]> findCountriesByProgramIds(
            @Param("ids") List<Long> ids
    );
}
