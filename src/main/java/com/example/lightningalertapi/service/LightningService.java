package com.example.lightningalertapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LightningService {
    List<String> lightningAlert(MultipartFile multipartFile) throws IOException;
}
