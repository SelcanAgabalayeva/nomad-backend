package nomad.example.nomad_backend.controller;

import nomad.example.nomad_backend.entity.UserProject;
import nomad.example.nomad_backend.entity.Wishlist;
import nomad.example.nomad_backend.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Wishlist> addToWishlist(
            @RequestParam Integer userId,
            @RequestParam Long projectId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(userId, projectId));
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<List<UserProject>> getWishlist(@PathVariable Integer userId) {
        return ResponseEntity.ok(wishlistService.getUserWishlist(userId));
    }
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeFromWishlist(
            @RequestParam Integer userId,
            @RequestParam Long projectId) {
        wishlistService.removeFromWishlist(userId, projectId);
        return ResponseEntity.noContent().build();
    }
}