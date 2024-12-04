package org.braun.sekur.sekurserver.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class ArchiveService {

    @Value("${storage.dir:null}")
    private String storageDir;
    
    // TODO security

    public void backup(String device, MultipartFile inputFile) throws IOException {
        // TODO folder einkommentieren
//        String folder = inputFile.getName().split("\\.")[0];
            
        Path targetFolderPath = storageDir != null ? Path.of(storageDir) : Paths.get(".", "Backups");
        File file = new File(Path.of(targetFolderPath.toString(), device, inputFile.getOriginalFilename()).toString());
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists())
            parentDir.mkdirs();

        if (!file.exists())
            file.createNewFile();

        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(inputFile.getBytes());
        }
    }
}