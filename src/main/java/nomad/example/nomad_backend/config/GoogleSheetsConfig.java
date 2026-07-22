package nomad.example.nomad_backend.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Configuration
public class GoogleSheetsConfig {

    @Value("${GOOGLE_CREDENTIALS:}")
    private String googleCredentials;


    @Bean
    public Sheets sheets() throws Exception {

        InputStream inputStream;

        if (googleCredentials != null && !googleCredentials.isBlank()) {

            inputStream = new ByteArrayInputStream(
                    googleCredentials.getBytes()
            );

        } else {

            inputStream = new ClassPathResource(
                    "google-credentials.json"
            ).getInputStream();

        }


        GoogleCredential credential = GoogleCredential
                .fromStream(inputStream)
                .createScoped(List.of(SheetsScopes.SPREADSHEETS));


        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        )
                .setApplicationName("nomad-backend")
                .build();
    }
}