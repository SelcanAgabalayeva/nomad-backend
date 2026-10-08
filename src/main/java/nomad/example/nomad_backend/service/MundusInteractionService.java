package nomad.example.nomad_backend.service;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.MundusUserStatusResponse;
import nomad.example.nomad_backend.entity.MundusFavorite;
import nomad.example.nomad_backend.entity.MundusProgram;
import nomad.example.nomad_backend.entity.MundusSaved;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.repository.MundusFavoriteRepository;
import nomad.example.nomad_backend.repository.MundusProgramRepository;
import nomad.example.nomad_backend.repository.MundusSavedRepository;
import nomad.example.nomad_backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MundusInteractionService {

    private final MundusFavoriteRepository favoriteRepository;
    private final MundusSavedRepository savedRepository;
    private final MundusProgramRepository programRepository;
    private final UserRepository userRepository;

    /*
     * ============================
     * FAVORITE
     * ============================
     */

    public void addFavorite(Long programId, String email) {

        User user = getUser(email);

        MundusProgram program = getProgram(programId);

        boolean alreadyFavorite =
                favoriteRepository.existsByUser_IdAndProgram_Id(
                        user.getId(),
                        program.getId()
                );

        if (alreadyFavorite) {
            return;
        }

        MundusFavorite favorite = MundusFavorite.builder()
                .user(user)
                .program(program)
                .build();

        favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long programId, String email) {

        User user = getUser(email);

        if (!favoriteRepository.existsByUser_IdAndProgram_Id(
                user.getId(),
                programId
        )) {
            return;
        }

        favoriteRepository.deleteByUser_IdAndProgram_Id(
                user.getId(),
                programId
        );
    }

    @Transactional(readOnly = true)
    public List<MundusProgram> getFavorites(String email) {

        User user = getUser(email);

        return favoriteRepository.findFavoritePrograms(
                user.getId()
        );
    }

    /*
     * ============================
     * SAVE / BOOKMARK
     * ============================
     */

    public void addSaved(Long programId, String email) {

        User user = getUser(email);

        MundusProgram program = getProgram(programId);

        boolean alreadySaved =
                savedRepository.existsByUser_IdAndProgram_Id(
                        user.getId(),
                        program.getId()
                );

        if (alreadySaved) {
            return;
        }

        MundusSaved saved = MundusSaved.builder()
                .user(user)
                .program(program)
                .build();

        savedRepository.save(saved);
    }

    public void removeSaved(Long programId, String email) {

        User user = getUser(email);

        if (!savedRepository.existsByUser_IdAndProgram_Id(
                user.getId(),
                programId
        )) {
            return;
        }

        savedRepository.deleteByUser_IdAndProgram_Id(
                user.getId(),
                programId
        );
    }

    @Transactional(readOnly = true)
    public List<MundusProgram> getSaved(String email) {

        User user = getUser(email);

        return savedRepository.findSavedPrograms(
                user.getId()
        );
    }

    /*
     * ============================
     * DETAIL STATUS
     * ============================
     */

    @Transactional(readOnly = true)
    public MundusUserStatusResponse getUserStatus(
            Long programId,
            String email
    ) {

        User user = getUser(email);

        getProgram(programId);

        boolean favorite =
                favoriteRepository.existsByUser_IdAndProgram_Id(
                        user.getId(),
                        programId
                );

        boolean saved =
                savedRepository.existsByUser_IdAndProgram_Id(
                        user.getId(),
                        programId
                );

        return MundusUserStatusResponse.builder()
                .favorite(favorite)
                .saved(saved)
                .build();
    }

    /*
     * ============================
     * HELPERS
     * ============================
     */

    private User getUser(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "İstifadəçi tapılmadı"
                        )
                );
    }

    private MundusProgram getProgram(Long programId) {

        MundusProgram program =
                programRepository.findById(programId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Mundus proqramı tapılmadı: "
                                                + programId
                                )
                        );

        if (!Boolean.TRUE.equals(program.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Bu Mundus proqramı artıq aktiv deyil"
            );
        }

        return program;
    }
}
