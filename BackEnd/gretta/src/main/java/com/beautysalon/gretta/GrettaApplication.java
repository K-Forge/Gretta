package com.beautysalon.gretta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GrettaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrettaApplication.class, args);
	}

}
