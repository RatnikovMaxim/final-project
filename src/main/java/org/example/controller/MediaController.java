package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.MediaUploadResponseDTO;
import org.example.exception.AccountRemovedException;
import org.example.exception.ForbiddenException;
import org.example.manager.MediaManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
public class MediaController {
    private MediaManager manager;

    @RequestMapping("/media/upload-multipart")
    public MediaUploadResponseDTO uploadMultipart(MultipartFile file) {
        return new MediaUploadResponseDTO();
    }

    //http://localhost:8080/uploaded.png
    //http://localhost:8080/8514ec13-4945-48f9-8ba1-356dfe198ff2.png
    @RequestMapping("/media/upload-data")
    public MediaUploadResponseDTO uploadBytes(@RequestBody byte[] data) throws IOException, ForbiddenException, AccountRemovedException {
        return manager.upload(data);
    }
}
