package com.realestate.backendrealestate.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public String saveFile(MultipartFile file, String fileName) {
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