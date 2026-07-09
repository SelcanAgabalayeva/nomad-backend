package nomad.example.nomad_backend.controller;

import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/saved")
    public ResponseEntity<List<UserProject>> getSavedProjects() {
        return ResponseEntity.ok(projectService.getSavedProjects());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<UserProject>> getUpcomingDeadlines() {
        return ResponseEntity.ok(projectService.getUpcomingDeadlines());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<UserProject> updateStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {
        return ResponseEntity.ok(projectService.updateProjectStatus(id, status));
    }
}