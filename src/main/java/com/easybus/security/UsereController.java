package com.easybus.security;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userss")
public class UsereController {

	private final UserServicee userService;

	public UsereController(UserServicee userService) {
		this.userService = userService;
	}

	@PostMapping
	public UserE createUser(@RequestBody UserE user) {
		return userService.saveUser(user);
	}

	@GetMapping("/{id}")
	public UserE getUser(@PathVariable Long id) {
		return userService.getUser(id);
	}

	@GetMapping
	public List<UserE> getAllUsers() {
		return userService.getAllUsers();
	}
}
