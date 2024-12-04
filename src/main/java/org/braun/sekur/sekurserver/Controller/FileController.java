package org.braun.sekur.sekurserver.Controller;

import lombok.RequiredArgsConstructor;
import org.braun.sekur.sekurserver.Service.ArchiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;


@RequiredArgsConstructor
@RestController
public class FileController {
    private final ArchiveService archiveService;
    
    @PostMapping(value = "/archive")
    public ResponseEntity<?> acceptArchive(@RequestParam String device,
                                           @RequestBody MultipartFile file) throws IOException, URISyntaxException {
        archiveService.backup(device, file);
        return ResponseEntity.ok().build();
    }
}
