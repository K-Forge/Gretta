package com.beautysalon.gretta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {BatchAutoConfiguration.class})
@EnableScheduling
public class GrettaApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrettaApplication.class, args);
	}

}
