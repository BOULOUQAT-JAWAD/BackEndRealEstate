package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.PropertyImages;
import com.realestate.backendrealestate.repositories.PropertyImagesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Validated
public class PropertyImagesService {

    private final PropertyImagesRepository propertyImagesRepository;

    public void updatePropertyImages(Property property, List<MultipartFile> images) {
        List<PropertyImages> existingImages = propertyImagesRepository.findByProperty(property);

        // Convert the incoming images to a list of their generated filenames for easier comparison
        List<String> newImageFilePaths = images.stream()
                .map(image -> generateFilePath(property, image))
                .collect(Collectors.toList());

        // Handle image deletions
        existingImages.stream()
                .filter(existingImage -> !newImageFilePaths.contains(existingImage.getImage()))
                .forEach(this::deleteImage);

        // Save the new images and retain existing ones
        images.forEach(image -> {
            if (!isImageAlreadyStored(existingImages, generateFilePath(property, image))) {
                saveNewImage(property, image);
            }
        });
    }

    private String generateFilePath(Property property, MultipartFile image) {
        String directoryName = "PropertyImages";
        String fileName = "property_" + property.getPropertyId();
        return FileService.generateFilePath(image, fileName, directoryName);
    }

    private boolean isImageAlreadyStored(List<PropertyImages> existingImages, String imageFilePath) {
        return existingImages.stream()
                .anyMatch(existingImage -> existingImage.getImage().equals(imageFilePath));
    }

    private void saveNewImage(Property property, MultipartFile image) {
        if (!image.isEmpty()) {
            String filePath = FileService.saveFile(image, "property_" + property.getPropertyId(), "PropertyImages");
            PropertyImages propertyImage = new PropertyImages();
            propertyImage.setProperty(property);
            propertyImage.setImage(filePath);
            propertyImagesRepository.save(propertyImage);
        }
    }

    public void deleteImagesForProperty(Property property) {
        List<PropertyImages> propertyImages = propertyImagesRepository.findByProperty(property);
        propertyImages.forEach(this::deleteImage);
    }

    private void deleteImage(PropertyImages image) {
        FileService.deleteFile(image.getImage(), "PropertyImages");
        propertyImagesRepository.delete(image);
    }
}

