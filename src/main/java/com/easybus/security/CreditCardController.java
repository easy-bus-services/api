package com.easybus.security;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class CreditCardController {

	private final CreditCardService creditCardService;

	public CreditCardController(CreditCardService creditCardService) {
		this.creditCardService = creditCardService;
	}

	@PostMapping
	public CreditCard createCard(@RequestBody CreditCard card) {
	    return creditCardService.saveCard(card);
	}

	@GetMapping("/{id}")
	public CreditCard getCard(@PathVariable Long id) {
		return creditCardService.getCard(id);
	}

	@GetMapping
	public List<CreditCard> getAllCards() {
		return creditCardService.getAllCards();
	}
}
