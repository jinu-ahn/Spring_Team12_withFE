package com.example.spring_team12_withfe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class SpringTeam12WithFeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringTeam12WithFeApplication.class, args);
	}

}
