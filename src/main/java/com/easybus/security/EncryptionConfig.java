package com.easybus.security;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class EncryptionConfig {

    private final EncryptionService encryptionService;

    public EncryptionConfig(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostConstruct
    public void init() {
        EncryptionListener.setEncryptionService(encryptionService);
    }
}
