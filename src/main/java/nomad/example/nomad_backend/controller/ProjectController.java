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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/saved")
    public ResponseEntity<List<UserProject>> getSavedProjects() {
        return ResponseEntity.ok(projectService.getSavedProjects());
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

    // YENİ: konkret istifadəçinin BÜTÜN UserProject sətirlərini (bütün
    // statuslar - SAVED/PREPARING/APPLIED/ACCEPTED/REJECTED) qaytarır.
    // Frontend-in ApplicationStatusContext-i tətbiq açılanda BİR DƏFƏ
    // bunu çağırır. NOT: yuxarıdakı /saved və /applied endpoint-ləri
    // hazırda istifadəçiyə görə FİLTRLƏNMİR (bütün istifadəçilərin
    // məlumatını qarışdırır) - bu, ayrıca diqqət tələb edən mövcud bir
    // məsələdir, bu dəyişikliklə əlaqəli deyil.
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/mine")
    public ResponseEntity<List<UserProject>> getMyProjects(@RequestParam Long userId) {
        return ResponseEntity.ok(projectService.getUserProjects(userId));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<UserProject> updateStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {
        return ResponseEntity.ok(projectService.updateProjectStatus(id, status));
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/status")
    public ResponseEntity<UserProject> setStatus(
            @RequestParam Integer userId,
            @RequestParam Long opportunityId,
            @RequestParam ProjectStatus status) {
        return ResponseEntity.ok(projectService.setProjectStatus(userId, opportunityId, status));
    }
}