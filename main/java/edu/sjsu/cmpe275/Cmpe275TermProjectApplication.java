package edu.sjsu.cmpe275;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Cmpe275TermProjectApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(Cmpe275TermProjectApplication.class, args);
	}
}
