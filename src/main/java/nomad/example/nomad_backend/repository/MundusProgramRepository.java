package nomad.example.nomad_backend.repository;
import nomad.example.nomad_backend.entity.MundusProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MundusProgramRepository
        extends JpaRepository<MundusProgram, Long> {

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

}
