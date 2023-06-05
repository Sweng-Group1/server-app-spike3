package com.sweng22g1.spike3;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.sweng22g1.spike3.model.Role;
import com.sweng22g1.spike3.model.User;
import com.sweng22g1.spike3.service.UserService;

@SpringBootApplication
public class Spike3Application {

	public static void main(String[] args) {
		SpringApplication.run(Spike3Application.class, args);
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new Role(null, "SuperAdmin"));
			userService.saveRole(new Role(null, "Admin"));
			userService.saveRole(new Role(null, "Manager"));
			userService.saveRole(new Role(null, "User"));

			userService.saveUser(new User(null, "Sidharth", "sid", "password123", new ArrayList<>()));
			userService.saveUser(new User(null, "User", "usr", "123qweasdzxc", new ArrayList<>()));
			userService.saveUser(new User(null, "John Doe", "jdoe", "ijofhu733", new ArrayList<>()));
			userService.saveUser(new User(null, "Ava Smith", "asmith", "123erfwrgji", new ArrayList<>()));

			userService.addRoleToUser("sid", "SuperAdmin");
			userService.addRoleToUser("jdoe", "Admin");
			userService.addRoleToUser("asmith", "Manager");
			userService.addRoleToUser("usr", "User");
			userService.addRoleToUser("sid", "User");
			userService.addRoleToUser("jdoe", "User");
			userService.addRoleToUser("asmith", "User");

		};
	}

}
