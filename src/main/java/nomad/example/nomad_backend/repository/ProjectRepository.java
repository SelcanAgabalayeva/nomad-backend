package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<UserProject, Long> {

    List<UserProject> findByStatus(nomad.example.nomad_backend.entity.ProjectStatus status);

    List<UserProject> findByUserId(Long userId);

    List<UserProject> findByCategoryIn(List<String> categories);

    @Query("SELECT p FROM UserProject p WHERE p.deadline BETWEEN :now AND :upcoming")
    List<UserProject> findUpcomingDeadlines(
            @Param("now") LocalDateTime now,
            @Param("upcoming") LocalDateTime upcoming
    );
}