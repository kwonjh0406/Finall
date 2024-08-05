package com.kwonjh0406.finall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinallApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinallApplication.class, args);
	}

}
