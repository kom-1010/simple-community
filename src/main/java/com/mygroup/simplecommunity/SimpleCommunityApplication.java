package com.mygroup.simplecommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SimpleCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleCommunityApplication.class, args);
	}

}
