package org.example.manager;

import org.apache.tika.Tika;
import org.example.dto.MediaUploadResponseDTO;
import org.example.authentication.Authentication;
import org.example.authenticator.Authenticator;
import org.example.exception.AccountRemovedException;
import org.example.exception.ForbiddenException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class MediaManager {
    private Tika tika = new Tika();
    public MediaManager() throws IOException {
        Files.createDirectories(Paths.get("media"));
    }

    public MediaUploadResponseDTO upload(byte[] data) throws IOException, AccountRemovedException, ForbiddenException {
        String name = UUID.randomUUID().toString();
        String mime = tika.detect(data);
        if (mime.equals("image/png")) {
            name = name + ".png";
        }
        Path path = Paths.get("media",name);
        Files.write(path, data);
        return new MediaUploadResponseDTO(name);
    }

}
