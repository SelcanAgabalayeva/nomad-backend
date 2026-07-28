package nomad.example.nomad_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSettings {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;



    private boolean emailNotifications = true;

    private boolean inAppNotifications = true;




    private boolean newOpportunities = true;

    private boolean deadlineReminders = true;

    private boolean savedProjectChanges = true;

    private boolean platformUpdates = true;

}
