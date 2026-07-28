package nomad.example.nomad_backend.service.impls;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {


    private final String uploadDir = "uploads/profile/";


    public String save(MultipartFile file) throws IOException {


        String filename =
                UUID.randomUUID()
                        + "_"
                        + file.getOriginalFilename();


        Path filePath =
                Paths.get(uploadDir + filename);


        Files.createDirectories(
                filePath.getParent()
        );


        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );


        return "/uploads/profile/" + filename;
    }
}