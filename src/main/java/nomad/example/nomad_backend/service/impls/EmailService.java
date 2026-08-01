package nomad.example.nomad_backend.service.impls;

import lombok.RequiredArgsConstructor;
import nomad.example.nomad_backend.dtos.ContactMessageRequest;
import nomad.example.nomad_backend.entity.ContactMessage;
import nomad.example.nomad_backend.repository.ContactMessageRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ContactMessageRepository contactMessageRepository;

    public void sendDeadlineReminder(String email, String title) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Project Deadline Reminder");
        message.setText(
                "Your saved project \"" + title + "\" has a deadline tomorrow."
        );

        mailSender.send(message);
    }

    @Transactional
    public void sendAndSaveContactMessage(ContactMessageRequest request) {


        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setName(request.getName());
        contactMessage.setEmail(request.getEmail());
        contactMessage.setSubject(request.getSubject());
        contactMessage.setMessage(request.getMessage());

        contactMessageRepository.save(contactMessage); // Bazaya yadda saxlanılır

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("nomadyouth26@gmail.com");

        String subject = (request.getSubject() != null && !request.getSubject().trim().isEmpty())
                ? request.getSubject()
                : "Yeni Əlaqə Formu Mesajı";
        message.setSubject(subject);

        String emailContent = "Kimdən: " + request.getName() + "\n" +
                "E-poçt: " + request.getEmail() + "\n\n" +
                "Mesaj:\n" + request.getMessage();
        message.setText(emailContent);
        message.setReplyTo(request.getEmail());

        mailSender.send(message);
    }
    public void sendVerificationEmail(String email, String token) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "E-mail təsdiqi"
        );

        String verificationLink =
                "https://nomadyouth.com.az/verify-email?token="
                        + token;


        message.setText(
                "Salam!\n\n" +
                        "Hesabınızı aktivləşdirmək üçün aşağıdakı linkə daxil olun:\n\n" +
                        verificationLink +
                        "\n\nBu link 24 saat ərzində keçərlidir."
        );


        mailSender.send(message);
    }
    public void sendInterestNotification(String email, String title) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Maraq dairənizə uyğun yeni layihə"
        );

        message.setText(
                "Salam!\n\n" +
                        "Maraq dairənizə uyğun yeni layihə əlavə edildi:\n\n" +
                        title +
                        "\n\nPlatformaya daxil olaraq ətraflı baxa bilərsiniz."
        );

        mailSender.send(message);
    }
}