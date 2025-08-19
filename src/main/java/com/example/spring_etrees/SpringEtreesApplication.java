package com.example.spring_etrees;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringEtreesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEtreesApplication.class, args);
	}

}
