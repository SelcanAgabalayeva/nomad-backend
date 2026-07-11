package nomad.example.nomad_backend.service;

import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.repository.ProjectRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final MessageSource messageSource;

    public ProjectService(ProjectRepository projectRepository, MessageSource messageSource) {
        this.projectRepository = projectRepository;
        this.messageSource = messageSource;
    }

    public List<UserProject> getSavedProjects() {
        return projectRepository.findByStatus(ProjectStatus.SAVED);
    }

    public List<UserProject> getAppliedProjects() {
        return projectRepository.findByStatus(ProjectStatus.APPLIED);
    }

    public List<UserProject> getRecommendedProjects(List<String> categories) {
        return projectRepository.findByCategoryIn(categories);
    }

    public UserProject updateProjectStatus(Long projectId, ProjectStatus newStatus) {
        UserProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("project.not.found", null, LocaleContextHolder.getLocale())
                ));

        project.setStatus(newStatus);
        project.setUpdatedAt(LocalDateTime.now());
        return projectRepository.save(project);
    }
}