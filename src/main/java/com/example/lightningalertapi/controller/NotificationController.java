package com.example.lightningalertapi.controller;

import com.example.lightningalertapi.exception.ServiceException;
import com.example.lightningalertapi.service.LightningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class NotificationController {

    @Autowired
    LightningService lightningService;

    @PostMapping("/lightning-alert")
    public ResponseEntity<Object> lightningAlert(@RequestPart("file")MultipartFile file) throws ServiceException {
        return new ResponseEntity<>(lightningService.lightningAlert(file), HttpStatus.OK);
    }
}
