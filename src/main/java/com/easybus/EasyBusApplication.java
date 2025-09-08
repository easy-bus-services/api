package com.easybus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

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
public class EasyBusApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EasyBusApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	    return application.sources(EasyBusApplication.class);
	}
}
