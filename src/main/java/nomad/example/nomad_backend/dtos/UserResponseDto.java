package nomad.example.nomad_backend.dtos;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {

    private Integer id;

    private String username;

    private String email;

    private LocalDateTime createdAt;
}

