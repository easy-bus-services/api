package com.easybus.security;


import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE_BYTES = 16;

    @Value("${encryption.secret-key}")
    private String base64Key;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    private byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE_BYTES];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public String encrypt(String plainText) throws Exception {
        byte[] iv = generateIV();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        byte[] combined = new byte[iv.length + encryptedBytes.length];

        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);

        return Base64.getEncoder().encodeToString(combined);
    }

    public String decrypt(String encryptedCombinedText) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedCombinedText);

        byte[] iv = new byte[IV_SIZE_BYTES];
        System.arraycopy(combined, 0, iv, 0, IV_SIZE_BYTES);

        byte[] encryptedBytes = new byte[combined.length - IV_SIZE_BYTES];
        System.arraycopy(combined, IV_SIZE_BYTES, encryptedBytes, 0, encryptedBytes.length);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        return new String(cipher.doFinal(encryptedBytes));
    }
    
    
    
//    public String decrypt(String encryptedData) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] decoded = Base64.getDecoder().decode(encryptedData);
//        return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
//    }
}
