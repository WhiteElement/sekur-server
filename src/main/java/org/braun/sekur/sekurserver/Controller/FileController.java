package org.braun.sekur.sekurserver.Controller;

import org.braun.sekur.sekurserver.Config.ArgsExtractor;
import org.braun.sekur.sekurserver.Service.ArchiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;


@RestController
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final ArchiveService archiveService;
    private final ArgsExtractor argsExtractor;
    
    
    public FileController (ArchiveService archiveService, ArgsExtractor argsExtractor) throws Exception {
        this.archiveService = archiveService;
        this.argsExtractor = argsExtractor;
        String apiKey = argsExtractor.getArg("apiKey").orElseThrow(() -> new Exception("apiKey not defined"));
        logger.atInfo().log("apiKey: '{}'", apiKey);
    }
    
    @PostMapping(value = "/archive")
    public ResponseEntity<?> acceptArchive(@RequestHeader("apiKey") String apiKey,
                                           @RequestParam("file") MultipartFile file) throws IOException {
        
        if (apiKey == null)
            return ResponseEntity.badRequest().body("no 'apiKey' provided");

        if (!Objects.equals(argsExtractor.getArg("apiKey").orElseThrow(), apiKey))
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Invalid apiKey");
        
        archiveService.backup(file);
        return ResponseEntity.ok().build();
    }
}
