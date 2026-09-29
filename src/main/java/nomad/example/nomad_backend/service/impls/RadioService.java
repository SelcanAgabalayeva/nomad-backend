package nomad.example.nomad_backend.service.impls;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.RadioTrackRequest;
import nomad.example.nomad_backend.dtos.RadioTrackResponse;
import nomad.example.nomad_backend.entity.RadioTrack;
import nomad.example.nomad_backend.repository.RadioTrackRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RadioService {

    private final RadioTrackRepository radioTrackRepository;
    private final Cloudinary cloudinary;

    public List<RadioTrackResponse> getAllTracks() {

        return radioTrackRepository
                .findAllByOrderByPositionAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RadioTrackResponse createTrack(
            RadioTrackRequest request,
            MultipartFile audioFile
    ) throws IOException {

        if (audioFile == null || audioFile.isEmpty()) {
            throw new IllegalArgumentException("MP3 file is required");
        }

        String contentType = audioFile.getContentType();

        if (contentType == null ||
                !contentType.equalsIgnoreCase("audio/mpeg")) {

            throw new IllegalArgumentException(
                    "Only MP3 files are allowed"
            );
        }

        Map uploadResult = cloudinary.uploader().upload(
                audioFile.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "video",
                        "folder", "nomad-radio"
                )
        );

        String audioUrl =
                uploadResult.get("secure_url").toString();

        RadioTrack track = RadioTrack.builder()
                .title(request.getTitle())
                .artist(request.getArtist())
                .audioUrl(audioUrl)
                .coverUrl(request.getCoverUrl())
                .position(request.getPosition())
                .build();

        RadioTrack savedTrack =
                radioTrackRepository.save(track);

        return toResponse(savedTrack);
    }

    private RadioTrackResponse toResponse(RadioTrack track) {

        return RadioTrackResponse.builder()
                .id(track.getId())
                .title(track.getTitle())
                .artist(track.getArtist())
                .audioUrl(track.getAudioUrl())
                .coverUrl(track.getCoverUrl())
                .position(track.getPosition())
                .build();
    }
    public void deleteTrack(Long id) throws IOException {

        RadioTrack track = radioTrackRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Radio track not found")
                );

        radioTrackRepository.delete(track);
    }
    public RadioTrackResponse updateTrack(
            Long id,
            RadioTrackRequest request,
            MultipartFile audioFile
    ) throws IOException {

        RadioTrack track = radioTrackRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Radio track not found")
                );

        track.setTitle(request.getTitle());
        track.setArtist(request.getArtist());
        track.setCoverUrl(request.getCoverUrl());
        track.setPosition(request.getPosition());

        if (audioFile != null && !audioFile.isEmpty()) {

            String contentType = audioFile.getContentType();

            if (contentType == null ||
                    !contentType.equalsIgnoreCase("audio/mpeg")) {

                throw new IllegalArgumentException(
                        "Only MP3 files are allowed"
                );
            }

            Map uploadResult = cloudinary.uploader().upload(
                    audioFile.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "video",
                            "folder", "nomad-radio"
                    )
            );

            String audioUrl =
                    uploadResult.get("secure_url").toString();

            track.setAudioUrl(audioUrl);
        }

        RadioTrack updatedTrack =
                radioTrackRepository.save(track);

        return toResponse(updatedTrack);
    }
}
