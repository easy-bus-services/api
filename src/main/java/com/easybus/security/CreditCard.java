package com.easybus.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@EntityListeners(EncryptionListener.class)
 @Table(name = "credit_card") 
@Data
public class CreditCard {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
   	@Column(name = "card_holder")
    private String cardHolder;

    @EncryptedField
 	@Column(name = "card_number")
    private String cardNumber;
 
    @EncryptedField
	@Column(name = "cvv")
    private String cvv;

    // getters and setters
}
