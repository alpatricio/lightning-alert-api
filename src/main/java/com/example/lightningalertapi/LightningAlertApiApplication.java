package com.example.lightningalertapi;

import com.example.lightningalertapi.service.LightningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SpringBootApplication
@RestController
public class LightningAlertApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightningAlertApiApplication.class, args);
    }

    @Autowired
    LightningService lightningService;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) throws IOException {
        return String.format("Hello %s!", name);
    }

    @PostMapping("/lightning-alert")
    public ResponseEntity<String> lightningAlert(@RequestPart("file") MultipartFile file) throws IOException {
        if (null == file.getOriginalFilename()) {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }

        lightningService.lightningAlert(file);


        return new ResponseEntity<>("Good Job", HttpStatus.OK);
    }
}
