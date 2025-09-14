package com.easybus.model;

import java.util.Set;

import lombok.Data;
@Data
public class UserSignupRequest {
	 private String fullName;
	    private String email;
	    private String phoneNumber;
	    private String password;
	    private Set<String> roles; // e.g., ["ROLE_USER", "ROLE_ADMIN"]
	}
