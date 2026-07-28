package nomad.example.nomad_backend.listener;

import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.event.OpportunityCreatedEvent;
import nomad.example.nomad_backend.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpportunityNotificationListener {

    private final WishlistRepository wishlistRepository;

    @Async
    @EventListener
    public void handleOpportunityCreatedEvent(OpportunityCreatedEvent event) {
        Opportunity newOpportunity = event.getOpportunity();

        log.info("Yeni layihə yaradıldı: {}. Uyğun istifadəçilər axtarılır...", newOpportunity.getTitle());

        List<User> targetUsers = wishlistRepository.findUsersWithMatchingPreferences(
                newOpportunity.getCountry(),
                newOpportunity.getType(),
                newOpportunity.getCategory()
        );

        for (User user : targetUsers) {
            log.info("Bildiriş göndərilir -> İstifadəçi ID: {}, Layihə: {}", user.getId(), newOpportunity.getTitle());
            // Burada notification service çağırılacaq
        }
    }
}