package com.doit.univplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude= SecurityAutoConfiguration.class)
public class UnivplannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnivplannerApplication.class, args);
	}

}
