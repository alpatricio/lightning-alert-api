package com.example.lightningalertapi.service;

import com.example.lightningalertapi.exception.ServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LightningService {
    List<String> lightningAlert(MultipartFile multipartFile) throws ServiceException;
}
