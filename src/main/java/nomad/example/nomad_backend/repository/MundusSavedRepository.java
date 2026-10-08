package nomad.example.nomad_backend.repository;
import nomad.example.nomad_backend.entity.MundusProgram;
import nomad.example.nomad_backend.entity.MundusSaved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MundusSavedRepository
        extends JpaRepository<MundusSaved, Long> {

    boolean existsByUser_IdAndProgram_Id(
            Long userId,
            Long programId
    );

    Optional<MundusSaved> findByUser_IdAndProgram_Id(
            Long userId,
            Long programId
    );

    void deleteByUser_IdAndProgram_Id(
            Long userId,
            Long programId
    );

    @Query("""
        SELECT s.program
        FROM MundusSaved s
        WHERE s.user.id = :userId
          AND s.program.active = true
        ORDER BY s.createdAt DESC
    """)
    List<MundusProgram> findSavedPrograms(
            @Param("userId") Long userId
    );
}
