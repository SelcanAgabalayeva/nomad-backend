package nomad.example.nomad_backend.controller;

import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.ProjectStatus;
import nomad.example.nomad_backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping("/{id}/save")
    public ResponseEntity<UserProject> saveProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.updateProjectStatus(id, ProjectStatus.SAVED));
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/applied")
    public ResponseEntity<List<UserProject>> getAppliedProjects() {
        return ResponseEntity.ok(projectService.getAppliedProjects());
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/recommended")
    public ResponseEntity<List<UserProject>> getRecommendedProjects(@RequestParam List<String> categories) {
        return ResponseEntity.ok(projectService.getRecommendedProjects(categories));
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<UserProject> updateStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {
        return ResponseEntity.ok(projectService.updateProjectStatus(id, status));
    }
}