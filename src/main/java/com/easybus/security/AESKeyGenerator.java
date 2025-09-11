//package com.easybus;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//public class AESKeyGenerator {
//    public static void main(String[] args) throws Exception {
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(256); // AES-128
//        SecretKey secretKey = keyGen.generateKey();
//        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        System.out.println("Base64 Encoded Key: " + base64Key);
//    }
//}



//    public UserServicee(usereRepository userRepository, EncryptionService encryptionService) {
//        this.userRepository = userRepository;
//        this.encryptionService = encryptionService;
//    }
//
//    public UserE saveUser(UserE user) throws Exception {
//        // Encrypt sensitive field
//        user.setSsn(encryptionService.encrypt(user.getSsn()));
//        return userRepository.save(user);
//    }
//
//    public UserE getUser(Long id) throws Exception {
//    	UserE user = userRepository.findById(id).orElseThrow();
//        // Decrypt before returning
//        user.setSsn(encryptionService.decrypt(user.getSsn()));
//        return user;
//    }


