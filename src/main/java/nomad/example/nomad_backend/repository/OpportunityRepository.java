package nomad.example.nomad_backend.repository;

import nomad.example.nomad_backend.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpportunityRepository extends JpaRepository<Opportunity, Long> {

    Optional<Opportunity> findByUniqueKey(String uniqueKey);
}
