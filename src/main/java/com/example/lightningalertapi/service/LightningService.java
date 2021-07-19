package com.example.lightningalertapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface LightningService {
    public void lightningAlert(MultipartFile multipartFile);
}
