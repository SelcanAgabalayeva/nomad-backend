package nomad.example.nomad_backend.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nomad.example.nomad_backend.entity.Opportunity;
import nomad.example.nomad_backend.entity.User;
import nomad.example.nomad_backend.event.OpportunityCreatedEvent;
import nomad.example.nomad_backend.repository.NotificationSettingsRepository;
import nomad.example.nomad_backend.repository.WishlistRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpportunityNotificationListener {

    private final WishlistRepository wishlistRepository;
    private final NotificationSettingsRepository notificationSettingsRepository;

    @Async
    @EventListener
    public void handleOpportunityCreatedEvent(OpportunityCreatedEvent event) {
        Opportunity opportunity = event.getOpportunity();

        log.info("Yeni fürsət yaradıldı: {}. Uyğun istifadəçilər axtarılır...", opportunity.getTitle());

        List<User> matchingUsers = wishlistRepository.findUsersWithMatchingPreferences(
                opportunity.getCountry(),
                opportunity.getType(),
                opportunity.getCategory()
        );

        for (User user : matchingUsers) {
            notificationSettingsRepository.findByUserId(user.getId()).ifPresent(settings -> {

                if (settings.isNewOpportunities()) {

                    log.info("İstifadəçiyə sayt içi bildiriş yaradılır: UserId = {}, Opportunity = {}",
                            user.getId(), opportunity.getTitle());

                }
            });
        }
    }
}