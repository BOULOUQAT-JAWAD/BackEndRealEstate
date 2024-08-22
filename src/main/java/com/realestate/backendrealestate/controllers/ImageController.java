package com.realestate.backendrealestate.controllers;
import com.realestate.backendrealestate.services.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> upload(@RequestParam(name = "images") List<MultipartFile> images) {
        log.info("Uploading Images");
        return ResponseEntity.ok(imageService.upload(images));
    }
}
