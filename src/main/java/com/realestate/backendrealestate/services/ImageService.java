package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.entities.Image;
import com.realestate.backendrealestate.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@Validated
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final FileService fileService;

    public List<String> upload(List<MultipartFile> images) {
        List<String> newImageUrls = images.stream()
                .map(image -> fileService.saveFile(image, "image"))
                .toList();

        newImageUrls.forEach(imageUrl -> {
            Image image = new Image();
            image.setUrl(imageUrl);
            imageRepository.save(image);
        });

        return newImageUrls;
    }

}