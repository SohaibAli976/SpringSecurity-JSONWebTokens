package com.sohaib.userservice;

import com.sohaib.userservice.domain.Roles;
import com.sohaib.userservice.domain.User;
import com.sohaib.userservice.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserService userService)
	{
		return args -> {
			userService.saveRole(new Roles("ROLE_USER"));
			userService.saveRole(new Roles("ROLE_MANAGER"));
			userService.saveRole(new Roles("ROLE_ADMIN"));
			userService.saveRole(new Roles("ROLE_SUPER_ADMIN"));

			userService.saveUser(new User(null, "john travolta", "john", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "will Smith", "will", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "jim Carry", "jim", "1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Arnold", "arnold", "1234", new ArrayList<>()));

			userService.addRoleToUser("john", "ROLE_USER");
			userService.addRoleToUser("will", "ROLE_MANAGER");
			userService.addRoleToUser("jim", "ROLE_ADMIN");
			userService.addRoleToUser("arnold", "ROLE_SUPER_ADMIN");
			userService.addRoleToUser("arnold", "ROLE_ADMIN");
			userService.addRoleToUser("arnold", "ROLE_USER");
		};
	}
}
