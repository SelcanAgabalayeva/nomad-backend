package nomad.example.nomad_backend.controller;

import nomad.example.nomad_backend.dtos.LikeResponse;
import nomad.example.nomad_backend.entity.Like;
import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.service.LikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/add")
    public ResponseEntity<LikeResponse> addLike(
            @RequestParam Long userId,
            @RequestParam Long projectId) {

        return ResponseEntity.ok(
                likeService.addLike(userId, projectId)
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserProject>> getUserLikes(@PathVariable Long userId) {
        return ResponseEntity.ok(likeService.getUserLikes(userId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeLike(
            @RequestParam Long userId,
            @RequestParam Long projectId) {
        likeService.removeLike(userId, projectId);
        return ResponseEntity.noContent().build();
    }
}