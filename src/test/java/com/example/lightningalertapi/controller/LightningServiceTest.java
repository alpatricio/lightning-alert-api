package com.example.lightningalertapi.controller;

import com.example.lightningalertapi.exception.ServiceException;
import com.example.lightningalertapi.repository.impl.AssetRepositoryImpl;
import com.example.lightningalertapi.service.impl.LightningServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class LightningServiceTest {

    @Mock
    private AssetRepositoryImpl repo;

    @InjectMocks
    private LightningServiceImpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test()
    void malformedFileJsonShouldThrowException() throws Exception {
        FileInputStream inputFile = new FileInputStream( "malformed.json");
        MockMultipartFile file = new MockMultipartFile("malformed.json", "malformed.json", "multipart/form-data", inputFile);

        Exception exception = assertThrows(ServiceException.class, () -> {
            service.lightningAlert(file);
        });

        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(LightningServiceImpl.FILE_NOT_IN_JSON));
    }

    @Test()
    void emptyFileShouldReturnEmptyList() throws Exception {
        FileInputStream inputFile = new FileInputStream( "empty.json");
        MockMultipartFile file = new MockMultipartFile("empty.json", "empty.json", "multipart/form-data", inputFile);
        assertTrue(service.lightningAlert(file).isEmpty());
    }
}
