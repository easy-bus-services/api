package com.easybus.security;

import java.lang.reflect.Field;

import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class EncryptionListener {

    private static EncryptionService encryptionService;

    public static void setEncryptionService(EncryptionService service) {
        encryptionService = service;
    }

    @PrePersist
    @PreUpdate
    public void encrypt(Object entity) throws Exception {
        processFields(entity, true); // encrypt
    }

    @PostLoad
    public void decrypt(Object entity) throws Exception {
        processFields(entity, false); // decrypt
    }

    private void processFields(Object entity, boolean encrypt) throws Exception {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(EncryptedField.class)) {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value != null) {
                    String processedValue = encrypt ?
                            encryptionService.encrypt(value.toString()) :
                            encryptionService.decrypt(value.toString());
                    field.set(entity, processedValue);
                }
            }
        }
    }
}
