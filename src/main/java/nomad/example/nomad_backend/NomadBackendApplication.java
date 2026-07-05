package nomad.example.nomad_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NomadBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NomadBackendApplication.class, args);
	}

}
