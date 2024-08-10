package com.realestate.backendrealestate.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileService {
    public static String generateFilePath(MultipartFile file, String fileName, String directoryName) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String fileFullName = fileName + "_" + now.format(formatter);

        File directory = new File(directoryName);
        String absolutePath = directory.getAbsolutePath();

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return absolutePath + "\\" + fileFullName + "." + fileExtension;
    }

    public static String saveFile(MultipartFile file, String fileName, String directoryName) {
        String filePath = generateFilePath(file, fileName, directoryName);
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }

    public static String updateFile(MultipartFile newFile, String fileName, String directoryName, String oldFilePath) {
        if (oldFilePath!=null){
            File oldFile = new File(oldFilePath);
            if (oldFile.exists())
                FileUtils.deleteQuietly(new File(oldFilePath));
        }
        return saveFile(newFile, fileName, directoryName);
    }

    public static boolean deleteFile(String filePath, String directoryName) {
        File file = new File(directoryName, filePath);
        if (file.exists()) {
            return file.delete();
        } else {
            return false; // File doesn't exist
        }
    }
}
