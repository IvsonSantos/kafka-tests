package com.example.kafka.controller;

import com.example.kafka.component.TextDataProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * responsible for handling HTTP requests.
 */
@RestController
public class TextDataController {

    private final TextDataProducer producer;

    public TextDataController(TextDataProducer producer) {
        this.producer = producer;
    }

    /**
     * is mapped to handle POST requests to /upload. It accepts a multipart file and uploads it.
     * creates a temporary file, transfers the uploaded file content to it,
     * sends the content of the temporary file to Kafka using the TextDataProducer,
     * and returns the path of the temporary file.
     */
    @PostMapping("/upload")
    public Optional<String> uploadTextFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile(file.getOriginalFilename(), null);
        file.transferTo(tempFile);
        Thread.ofVirtual().start(() ->
            producer.sendContentOf(tempFile.toFile())
        );
        return Optional.of(tempFile.toString());
    }

    @GetMapping
    public String get() {
        return "oi";
    }

}
