package org.braun.sekur.sekurserver.Service;

import org.apache.commons.io.FileUtils;
import org.braun.sekur.sekurserver.Controller.FileController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;


@Service
public class ArchiveService {

    @Value("${storage.dir:null}")
    private String storageDir;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    public void backup(MultipartFile inputFile) throws IOException {
        // IF file content is same as last file THEN return
        File tempFile = null;
        try {
            Path targetFolderPath = storageDir != null ? Path.of(storageDir) : Paths.get(".", "Backups");
            File latestBackup = findLatestFile(targetFolderPath);
            tempFile = new File(Path.of(targetFolderPath.toString(), "temp").toString());
            try (OutputStream os = Files.newOutputStream(tempFile.toPath())) {
                os.write(inputFile.getBytes());
            }

            if (FileUtils.contentEquals(tempFile, latestBackup)) {
                logger.atInfo().log("Contents match latest Backup at {} -> Not backing up", latestBackup.getAbsolutePath());
                return;
            }
            File newFile = new File(Path.of(targetFolderPath.toString(), inputFile.getOriginalFilename()).toString());

            File parentDirectory = newFile.getParentFile();

            if (!parentDirectory.exists() && parentDirectory != null)
                parentDirectory.mkdirs();

            if (!newFile.exists())
                newFile.createNewFile();

            try (OutputStream outputStream = new FileOutputStream(newFile)) {
                outputStream.write(inputFile.getBytes());
            }

            logger.atInfo().log("Backup of {} completed", inputFile.getOriginalFilename());
        } finally {
            tempFile.delete();
        }
    }

    private File findLatestFile(Path targetFolderPath) {
        File directory = new File(targetFolderPath.toString());
        File[] files = directory.listFiles();
        Objects.requireNonNull(files);
        
        if (files.length == 0)
            return new File("PLACEHOLDER");
        
        Arrays.sort(files, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        return files[0];
    }
}
