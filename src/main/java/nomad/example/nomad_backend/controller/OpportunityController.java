package nomad.example.nomad_backend.controller;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.repository.OpportunityRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/opportunities")
@RequiredArgsConstructor
public class OpportunityController {

    private final OpportunityRepository repository;

    @GetMapping
    public List<Opportunity> getAll() {
        return repository.findAll();
    }
}
