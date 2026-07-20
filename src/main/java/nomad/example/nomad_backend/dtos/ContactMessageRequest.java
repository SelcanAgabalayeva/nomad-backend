package nomad.example.nomad_backend.dtos;

import lombok.Data;

@Data
public class ContactMessageRequest {
    private String name;
    private String email;
    private String subject;
    private String message;
}
