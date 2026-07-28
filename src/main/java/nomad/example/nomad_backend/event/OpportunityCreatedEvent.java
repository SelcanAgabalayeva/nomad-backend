package nomad.example.nomad_backend.event;

import nomad.example.nomad_backend.entity.Opportunity;
import lombok.Getter;

@Getter
public class OpportunityCreatedEvent {
    private final Opportunity opportunity;

    public OpportunityCreatedEvent(Opportunity opportunity) {
        this.opportunity = opportunity;
    }
}