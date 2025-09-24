package com.jobportal.backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String storeResume(MultipartFile file);
}