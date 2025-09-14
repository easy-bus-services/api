package com.easybus.security;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/encryption")
public class EncryptionController {

    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody String plainText) throws Exception {
        return encryptionService.encrypt(plainText);
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestBody DecryptRequest request) throws Exception {
        return encryptionService.decrypt(request.getEncryptedText());
    }
    
//    @PostMapping("/saveCard")
//    public ResponseEntity<?> saveCard(@RequestBody Card card) throws Exception {
//        card.setNumber(encryptionService.encrypt(card.getNumber()));
//        cardRepository.save(card);
//        return ResponseEntity.ok("Saved Successfully");
//    }
//    @PostMapping("/decrypt")
//    public String decrypt(@RequestBody String encryptedText) throws Exception {
//        return encryptionService.decrypt(encryptedText);
//    }
}
