package nomad.example.nomad_backend.service;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.MundusProgramRequest;
import nomad.example.nomad_backend.entity.MundusProgram;
import nomad.example.nomad_backend.repository.MundusProgramRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MundusProgramService {
    private final MundusProgramRepository repository;

    @Transactional(readOnly = true)
    public List<MundusProgram> getAll(boolean includeInactive) {
        if (includeInactive) {
            return repository.findAllByOrderByCreatedAtDesc();
        }

        return repository.findAllByActiveTrueOrderByDeadlineAsc();
    }

    @Transactional(readOnly = true)
    public MundusProgram getById(Long id) {
        MundusProgram program = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mundus proqramı tapılmadı: " + id
                ));

        if (!Boolean.TRUE.equals(program.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Aktiv Mundus proqramı tapılmadı: " + id
            );
        }

        return program;
    }

    public MundusProgram create(MundusProgramRequest request) {
        validateRequest(request);

        MundusProgram program = new MundusProgram();
        copyRequestToEntity(request, program);

        if (request.getActive() == null) {
            program.setActive(true);
        }

        if (request.getScholarship() == null) {
            program.setScholarship(false);
        }

        if (request.getIeltsRequired() == null) {
            program.setIeltsRequired(false);
        }

        program.setViewCount(0L);
        program.setApplicationClickCount(0L);

        return repository.save(program);
    }

    public MundusProgram update(Long id, MundusProgramRequest request) {
        validateRequest(request);

        MundusProgram program = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mundus proqramı tapılmadı: " + id
                ));

        copyRequestToEntity(request, program);

        return repository.save(program);
    }
    public void toggleActive(Long id) {
        MundusProgram program = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mundus proqramı tapılmadı: " + id
                ));

        program.setActive(!Boolean.TRUE.equals(program.getActive()));
        repository.save(program);
    }
    public void delete(Long id) {
        MundusProgram program = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mundus proqramı tapılmadı: " + id
                ));

        // Məlumat bazasından fiziki silinmir, yalnız deaktiv edilir.
        program.setActive(false);
        repository.save(program);
    }

    public void trackView(Long id) {
        int updated = repository.incrementViewCount(id);

        if (updated == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Aktiv Mundus proqramı tapılmadı: " + id
            );
        }
    }

    public void trackApplicationClick(Long id) {
        int updated = repository.incrementApplicationClickCount(id);

        if (updated == 0) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Aktiv Mundus proqramı tapılmadı: " + id
            );
        }
    }

    private void validateRequest(MundusProgramRequest request) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Request boş ola bilməz"
            );
        }

        if (request.getProgramName() == null
                || request.getProgramName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "programName mütləq doldurulmalıdır"
            );
        }

        if (request.getDeadline() != null
                && request.getApplicationOpens() != null
                && request.getDeadline()
                .isBefore(request.getApplicationOpens())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "deadline applicationOpens tarixindən əvvəl ola bilməz"
            );
        }

        if (request.getIeltsScore() != null
                && (request.getIeltsScore() < 0
                || request.getIeltsScore() > 9)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "ieltsScore 0 ilə 9 arasında olmalıdır"
            );
        }

        if (request.getToeflScore() != null
                && (request.getToeflScore() < 0
                || request.getToeflScore() > 120)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "toeflScore 0 ilə 120 arasında olmalıdır"
            );
        }
    }

    private void copyRequestToEntity(
            MundusProgramRequest request,
            MundusProgram program
    ) {
        program.setProgramName(request.getProgramName().trim());
        program.setDescription(request.getDescription());
        program.setCategory(request.getCategory());
        program.setFieldOfStudy(request.getFieldOfStudy());

        program.setCountries(copyList(request.getCountries()));
        program.setUniversities(copyList(request.getUniversities()));
        program.setBachelorFields(copyList(request.getBachelorFields()));
        program.setRequiredDocuments(copyList(request.getRequiredDocuments()));

        program.setDegree(request.getDegree());
        program.setDuration(request.getDuration());
        program.setLanguage(request.getLanguage());

        program.setDeadline(request.getDeadline());
        program.setApplicationOpens(request.getApplicationOpens());
        program.setIntake(request.getIntake());

        program.setIeltsRequirement(request.getIeltsRequirement());
        program.setIeltsScore(request.getIeltsScore());

        if (request.getIeltsRequired() != null) {
            program.setIeltsRequired(request.getIeltsRequired());
        } else if (program.getIeltsRequired() == null) {
            program.setIeltsRequired(false);
        }

        program.setToeflRequirement(request.getToeflRequirement());
        program.setToeflScore(request.getToeflScore());

        if (request.getScholarship() != null) {
            program.setScholarship(request.getScholarship());
        } else if (program.getScholarship() == null) {
            program.setScholarship(false);
        }

        program.setScholarshipAmount(request.getScholarshipAmount());
        program.setApplicationFee(request.getApplicationFee());

        program.setOfficialWebsite(request.getOfficialWebsite());
        program.setApplyLink(request.getApplyLink());

        if (request.getActive() != null) {
            program.setActive(request.getActive());
        } else if (program.getActive() == null) {
            program.setActive(true);
        }
    }

    private List<String> copyList(List<String> values) {
        if (values == null) {
            return new ArrayList<>();
        }

        return values.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .toList();
    }

}

