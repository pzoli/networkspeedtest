package hu.infokristaly.networkspeedtest;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

@RestController
@RequestMapping("/api/speedtest")
@CrossOrigin(origins = "${app.cors.allowed-origins}") // Angular alapértelmezett port
public class SpeedTestController {

    @Value("${sampledata.path}")
    private String sampleDataPath;

    private Path testFile = null;

    public SpeedTestController() {
    }

    @PostConstruct
    public void init() {
        this.testFile = Paths.get(sampleDataPath);
    }

    @GetMapping("/init")
    public void initSampleData() throws IOException {
        // Generálunk 100MB véletlenszerű adatot
        byte[] data = new byte[1024 * 1024 * 100];
        new Random().nextBytes(data);

        if (!Files.exists(testFile)) {
            Files.write(testFile, data, StandardOpenOption.CREATE);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestBody byte[] data) {
        // Csak fogadjuk az adatot, a mérés a kliens oldalon történik
        return ResponseEntity.ok("Upload successful");
    }

    @GetMapping("/stream-download")
    public ResponseEntity<InputStreamResource> downloadInputStream() throws FileNotFoundException {
        // Generálunk 10MB véletlenszerű adatot
        InputStreamResource data = new InputStreamResource(new FileInputStream(testFile.toFile()));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test.bin")
                .body(data);
    }

}
