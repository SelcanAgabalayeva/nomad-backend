package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.RadioTrackRequest;
import nomad.example.nomad_backend.dtos.RadioTrackResponse;
import nomad.example.nomad_backend.service.impls.RadioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/radio")
@RequiredArgsConstructor
public class RadioController {

    private final RadioService radioService;

    @GetMapping
    public ResponseEntity<List<RadioTrackResponse>> getRadioTracks() {

        return ResponseEntity.ok(
                radioService.getAllTracks()
        );
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<RadioTrackResponse> createTrack(

            @RequestParam("title")
            String title,

            @RequestParam("artist")
            String artist,

            @RequestParam(value = "coverUrl", required = false)
            String coverUrl,

            @RequestParam("position")
            Integer position,

            @RequestParam("audioFile")
            MultipartFile audioFile

    ) throws IOException {

        RadioTrackRequest request = new RadioTrackRequest();

        request.setTitle(title);
        request.setArtist(artist);
        request.setCoverUrl(coverUrl);
        request.setPosition(position);

        return ResponseEntity.ok(
                radioService.createTrack(
                        request,
                        audioFile
                )
        );
    }
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<RadioTrackResponse> updateTrack(

            @PathVariable Long id,

            @RequestParam("title")
            String title,

            @RequestParam("artist")
            String artist,

            @RequestParam(value = "coverUrl", required = false)
            String coverUrl,

            @RequestParam("position")
            Integer position,

            @RequestParam(value = "audioFile", required = false)
            MultipartFile audioFile

    ) throws IOException {

        RadioTrackRequest request = new RadioTrackRequest();

        request.setTitle(title);
        request.setArtist(artist);
        request.setCoverUrl(coverUrl);
        request.setPosition(position);

        return ResponseEntity.ok(
                radioService.updateTrack(
                        id,
                        request,
                        audioFile
                )
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrack(
            @PathVariable Long id
    ) throws IOException {

        radioService.deleteTrack(id);

        return ResponseEntity.noContent().build();
    }
}