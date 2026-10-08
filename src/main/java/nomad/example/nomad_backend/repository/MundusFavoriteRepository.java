package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.MundusFavorite;
import nomad.example.nomad_backend.entity.MundusProgram;
import nomad.example.nomad_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MundusFavoriteRepository
        extends JpaRepository<MundusFavorite, Long> {

    boolean existsByUser_IdAndProgram_Id(
            Long userId,
            Long programId
    );

    Optional<MundusFavorite> findByUser_IdAndProgram_Id(
            Long userId,
            Long programId
    );

    void deleteByUser_IdAndProgram_Id(
            Long userId,
            Long programId
    );

    @Query("""
        SELECT f.program
        FROM MundusFavorite f
        WHERE f.user.id = :userId
          AND f.program.active = true
        ORDER BY f.createdAt DESC
    """)
    List<MundusProgram> findFavoritePrograms(
            @Param("userId") Long userId
    );
}
