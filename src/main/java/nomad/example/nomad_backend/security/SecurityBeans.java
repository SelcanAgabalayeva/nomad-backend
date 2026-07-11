package nomad.example.nomad_backend.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner testPassword(
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            System.out.println(
                    passwordEncoder.encode("123456")
            );
        };
    }
}

