package com.example.lightningalertapi.controller;

import com.example.lightningalertapi.service.LightningService;
import net.sf.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class NotificationController {

    @Autowired
    LightningService lightningService;

    @PostMapping("/lightning-alert")
    public ResponseEntity<Object> lightningAlert(@RequestPart("file") MultipartFile file) {
        if (null == file) {
            return new ResponseEntity<>("File Required", HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(lightningService.lightningAlert(file), HttpStatus.OK);
        } catch (IOException ioException) {
            return new ResponseEntity<>("Invalid File", HttpStatus.BAD_REQUEST);
        } catch (JSONException jsonException){
            return new ResponseEntity<>("File not in JSON format", HttpStatus.BAD_REQUEST);
        }
    }
}
