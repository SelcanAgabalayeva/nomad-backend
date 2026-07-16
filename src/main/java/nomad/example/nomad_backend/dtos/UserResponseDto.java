package nomad.example.nomad_backend.dtos;


import lombok.Getter;
import lombok.Setter;
import nomad.example.nomad_backend.entity.Role;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Role role;
}

