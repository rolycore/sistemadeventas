package com.shalomsolutiontech.sistemadeventas;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

	public static void main(String[] args) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder ();
		String rawPassword = "rolycore";
		String encodePassword = encoder.encode(rawPassword);
	
		System.out.println(encodePassword);
	}

}
