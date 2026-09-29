package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.RadioTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RadioTrackRepository
        extends JpaRepository<RadioTrack, Long> {

    List<RadioTrack> findAllByOrderByPositionAsc();
}
