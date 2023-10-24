package com.springboot.j2ee;

import com.springboot.j2ee.entity.Post;
import com.springboot.j2ee.entity.User;
import com.springboot.j2ee.enums.EPostVisibility;
import com.springboot.j2ee.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.sql.Timestamp;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class J2eeApplication {

	public static void main(String[] args) {
		SpringApplication.run(J2eeApplication.class, args);
	}



}
