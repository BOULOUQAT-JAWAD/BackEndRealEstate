package com.realestate.backendrealestate.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.nio.file.Path;

@Service
public class FileService {

    @Value("${app.domain.name}")
    private String domainName;

    @Value("${images.folder}")
    private String directoryName;

    private static final String IMAGES_PATH = "/images/";

    public String generateFileName(MultipartFile file, String fileName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String fileFullName = fileName + "_" + now.format(formatter);

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return fileFullName + "." + fileExtension;
    }

    /*public String saveFile(MultipartFile file, String fileName) {
        File directory = new File(directoryName);

        // Check if the directory exists
        if (!directory.exists() || !directory.isDirectory()) {
            throw new RuntimeException("Directory does not exist: " + directoryName);
        }

        String generatedFileName = generateFileName(file, fileName);
        String filePath = directory.getAbsolutePath() + File.separator + generatedFileName;

        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }

        // Return the URL of the saved file
        return domainName + IMAGES_PATH + generatedFileName;
    }*/

    public String saveFile(MultipartFile file, String fileName) {
        try {
            // Ensure the directory exists
            Path uploadDirectory = Paths.get(directoryName);
            if (!Files.exists(uploadDirectory)) {
                Files.createDirectories(uploadDirectory);
            }

            // Generate file name
            String generatedFileName = generateFileName(file, fileName);
            String filePath = uploadDirectory.resolve(generatedFileName).toString();

            // Save the file locally
            file.transferTo(new File(filePath));

            // Return the URL for accessing the file (no BackEndRealEstate)
            return domainName + IMAGES_PATH + generatedFileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }
    }

    public String updateFile(MultipartFile newFile, String fileName, String oldFilePath) {
        if (oldFilePath != null) {
            File oldFile = new File(directoryName, new File(oldFilePath).getName());
            if (oldFile.exists()) {
                FileUtils.deleteQuietly(oldFile);
            }
        }
        return saveFile(newFile, fileName);
    }

    public boolean deleteFile(String filePath) {
        File file = new File(directoryName, new File(filePath).getName());
        if (file.exists()) {
            return file.delete();
        } else {
            return false; // File doesn't exist
        }
    }
}