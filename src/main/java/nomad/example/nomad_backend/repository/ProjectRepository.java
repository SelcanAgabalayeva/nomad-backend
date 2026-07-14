package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<UserProject, Long> {
    List<UserProject> findByStatus(ProjectStatus status);
    List<UserProject> findByOpportunity_CategoryIn(List<String> categories);

    Collection<UserProject> findByUserId(Long userId);
}