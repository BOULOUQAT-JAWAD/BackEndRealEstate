package com.realestate.backendrealestate.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Service
public class FileService {

    @Value("${app.domain.name}")
    private String domainName;

    @Value("${images.folder}")
    private String directoryName;

    private static final String IMAGES_PATH = "/images/";

    private final Cloudinary cloudinary;

    public FileService(@Value("${cloudinary.cloud-name}") String cloudName,
                       @Value("${cloudinary.api-key}") String apiKey,
                       @Value("${cloudinary.api-secret}") String apiSecret) {
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public String generateFileName(MultipartFile file, String fileName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String fileFullName = fileName + "_" + now.format(formatter);

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return fileFullName + "." + fileExtension;
    }

    public String saveFile(MultipartFile file, String fileName) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", fileName,
                    "folder", "real-estate-images"
            ));
            log.info("*******************************************************");
            log.info("Images URL : ");
            log.info(uploadResult.get("url").toString());
            return uploadResult.get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
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