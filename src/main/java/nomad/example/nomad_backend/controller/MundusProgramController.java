package nomad.example.nomad_backend.controller;
import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.MundusProgramRequest;
import nomad.example.nomad_backend.dtos.MundusUserStatusResponse;
import nomad.example.nomad_backend.entity.MundusProgram;
import nomad.example.nomad_backend.service.MundusInteractionService;
import nomad.example.nomad_backend.service.MundusProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mundus-programs")
@RequiredArgsConstructor
public class MundusProgramController {

    private final MundusProgramService mundusProgramService;

    private final MundusInteractionService mundusInteractionService;

    @GetMapping
    public ResponseEntity<List<MundusProgram>> getAll(
            @RequestParam(defaultValue = "false")
            boolean includeInactive
    ) {

        return ResponseEntity.ok(
                mundusProgramService.getAll(includeInactive)
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<MundusProgram> getById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                mundusProgramService.getById(id)
        );
    }


    // =========================================================
    // CREATE
    // =========================================================

    @PostMapping
    public ResponseEntity<MundusProgram> create(
            @RequestBody MundusProgramRequest request
    ) {

        MundusProgram created =
                mundusProgramService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MundusProgram> update(
            @PathVariable Long id,
            @RequestBody MundusProgramRequest request
    ) {

        return ResponseEntity.ok(
                mundusProgramService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        mundusProgramService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/view")
    public ResponseEntity<Void> trackView(
            @PathVariable Long id
    ) {

        mundusProgramService.trackView(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/apply-click")
    public ResponseEntity<Void> trackApplicationClick(
            @PathVariable Long id
    ) {

        mundusProgramService.trackApplicationClick(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> addFavorite(
            @PathVariable Long id,
            Authentication authentication
    ) {

        mundusInteractionService.addFavorite(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}/favorite")
    public ResponseEntity<Void> removeFavorite(
            @PathVariable Long id,
            Authentication authentication
    ) {

        mundusInteractionService.removeFavorite(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<MundusProgram>> getFavorites(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                mundusInteractionService.getFavorites(
                        authentication.getName()
                )
        );
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<Void> addSaved(
            @PathVariable Long id,
            Authentication authentication
    ) {

        mundusInteractionService.addSaved(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}/save")
    public ResponseEntity<Void> removeSaved(
            @PathVariable Long id,
            Authentication authentication
    ) {

        mundusInteractionService.removeSaved(
                id,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/saved")
    public ResponseEntity<List<MundusProgram>> getSaved(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                mundusInteractionService.getSaved(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/{id}/user-status")
    public ResponseEntity<MundusUserStatusResponse> getUserStatus(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                mundusInteractionService.getUserStatus(
                        id,
                        authentication.getName()
                )
        );
    }
}
