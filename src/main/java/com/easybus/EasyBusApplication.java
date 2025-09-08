package com.easybus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
@OpenAPIDefinition(
        info = @Info(
        title = "EASY BUS SERVICE AP",
        version = "1.0",
        description = "Welcome to the Easy bus  Techlogies",
        contact = @Contact(name ="Easy bus IT Technology",email = "vali@gmail.com")))

@SpringBootApplication
public class EasyBusApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyBusApplication.class, args);
	}

}
