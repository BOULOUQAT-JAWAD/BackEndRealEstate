package com.realestate.backendrealestate.services;

import com.realestate.backendrealestate.entities.Property;
import com.realestate.backendrealestate.entities.PropertyImages;
import com.realestate.backendrealestate.repositories.PropertyImagesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class PropertyImagesService {

    private final PropertyImagesRepository propertyImagesRepository;
    private final FileService fileService;

    public PropertyImagesService(Environment environment, PropertyImagesRepository propertyImagesRepository, FileService fileService) {
        this.fileService = fileService;
        this.propertyImagesRepository = propertyImagesRepository;
    }

    public void updatePropertyImages(Property property, List<MultipartFile> images) {
        List<PropertyImages> existingImages = propertyImagesRepository.findByProperty(property);

        // Convert the incoming images to a list of their generated URLs for easier comparison
        List<String> newImageUrls = images.stream()
                .map(image -> fileService.saveFile(image, "property_" + property.getPropertyId()))
                .toList();

        // Handle image deletions
        existingImages.stream()
                .filter(existingImage -> !newImageUrls.contains(existingImage.getImage()))
                .forEach(this::deleteImage);

        // Save the new images and retain existing ones
        newImageUrls.forEach(imageUrl -> {
            if (!isImageAlreadyStored(existingImages, imageUrl)) {
                saveNewImage(property, imageUrl);
            }
        });
    }

    private boolean isImageAlreadyStored(List<PropertyImages> existingImages, String imageFilePath) {
        return existingImages.stream()
                .anyMatch(existingImage -> existingImage.getImage().equals(imageFilePath));
    }

    private void saveNewImage(Property property, String imageUrl) {
        PropertyImages propertyImage = new PropertyImages();
        propertyImage.setProperty(property);
        propertyImage.setImage(imageUrl);
        propertyImagesRepository.save(propertyImage);
    }

    public void deleteImagesForProperty(Property property) {
        List<PropertyImages> propertyImages = propertyImagesRepository.findByProperty(property);
        propertyImages.forEach(this::deleteImage);
    }

    private void deleteImage(PropertyImages image) {
        fileService.deleteFile(image.getImage());
        propertyImagesRepository.delete(image);
    }
}