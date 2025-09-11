package com.easybus.security;



import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserServicee {

    private final usereRepository userRepository;
   // private final EncryptionService encryptionService;


    public UserServicee(usereRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserE saveUser(UserE user) {
        return userRepository.save(user);
    }

    public UserE getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<UserE> getAllUsers() {
        return userRepository.findAll();
    }

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
    
    
}
