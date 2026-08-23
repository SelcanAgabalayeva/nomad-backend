package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.ContactMessageRequest;
import nomad.example.nomad_backend.entity.ContactMessage;
import nomad.example.nomad_backend.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final ContactMessageRepository contactMessageRepository;

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Value("${resend.from}")
    private String resendFrom;


    private final RestClient restClient =
            RestClient.builder()
                    .baseUrl("https://api.resend.com")
                    .build();


    // =========================================================
    // DEADLINE REMINDER
    // =========================================================

    public void sendDeadlineReminder(
            String email,
            String title,
            int days
    ) {

        String dayText;

        if (days == 0) {
            dayText = "bu gündür";
        } else if (days == 1) {
            dayText = "sabahdır";
        } else {
            dayText = days + " gün sonra olacaq";
        }


        String text =
                "Salam!\n\n" +
                        "Saxladığınız elanın son müraciət tarixi "
                        + dayText + ":\n\n" +
                        title +
                        "\n\n" +
                        "Gecikmədən müraciət etməyi unutmayın.\n\n" +
                        "Nomad Youth komandası";


        sendEmail(
                email,
                "Elanın son müraciət tarixi yaxınlaşır",
                text
        );
    }


    // =========================================================
    // EMAIL VERIFICATION
    // =========================================================

    public void sendVerificationEmail(
            String email,
            String token
    ) {

        String verificationLink =
                "https://nomadyouth.com.az/verify-email?token="
                        + token;


        String text =
                "Salam!\n\n" +
                        "Hesabınızı aktivləşdirmək üçün aşağıdakı linkə daxil olun:\n\n" +
                        verificationLink +
                        "\n\n" +
                        "Bu link 24 saat ərzində keçərlidir.";


        sendEmail(
                email,
                "E-mail təsdiqi",
                text
        );
    }


    // =========================================================
    // INTEREST NOTIFICATION
    // =========================================================

    public void sendInterestNotification(
            String email,
            String title
    ) {

        String text =
                "Salam!\n\n" +
                        "Maraq dairənizə uyğun yeni layihə əlavə edildi:\n\n" +
                        title +
                        "\n\n" +
                        "Platformaya daxil olaraq ətraflı baxa bilərsiniz.";


        sendEmail(
                email,
                "Maraq dairənizə uyğun yeni layihə",
                text
        );
    }


    // =========================================================
    // CONTACT MESSAGE
    // =========================================================

    @Transactional
    public void sendAndSaveContactMessage(
            ContactMessageRequest request
    ) {

        // Bazaya yadda saxla
        ContactMessage contactMessage =
                new ContactMessage();

        contactMessage.setName(
                request.getName()
        );

        contactMessage.setEmail(
                request.getEmail()
        );

        contactMessage.setSubject(
                request.getSubject()
        );

        contactMessage.setMessage(
                request.getMessage()
        );

        contactMessageRepository.save(
                contactMessage
        );


        String subject =
                request.getSubject() != null
                        && !request.getSubject()
                        .trim()
                        .isEmpty()
                        ? request.getSubject()
                        : "Yeni Əlaqə Formu Mesajı";


        String text =
                "Kimdən: "
                        + request.getName()
                        + "\n" +

                        "E-poçt: "
                        + request.getEmail()
                        + "\n\n" +

                        "Mesaj:\n"
                        + request.getMessage();


        sendEmail(
                "nomadyouth26@gmail.com",
                subject,
                text
        );
    }


    // =========================================================
    // COMMON SEND METHOD
    // =========================================================

    private void sendEmail(
            String email,
            String subject,
            String text
    ) {

        restClient.post()
                .uri("/emails")
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + resendApiKey
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .body(
                        Map.of(
                                "from", resendFrom,
                                "to", email,
                                "subject", subject,
                                "text", text
                        )
                )
                .retrieve()
                .toBodilessEntity();
    }
}