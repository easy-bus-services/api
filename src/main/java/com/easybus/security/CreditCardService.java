package com.easybus.security;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CreditCardService {

	private final CreditCardRepository creditCardRepository;

	public CreditCardService(CreditCardRepository creditCardRepository) {
		
		this.creditCardRepository = creditCardRepository;
	}

	public CreditCard saveCard(CreditCard card) {
		return creditCardRepository.save(card);
	}

	public CreditCard getCard(Long id) {
		return creditCardRepository.findById(id).orElse(null);
	}

	public List<CreditCard> getAllCards() {
		return creditCardRepository.findAll();
	}
}
