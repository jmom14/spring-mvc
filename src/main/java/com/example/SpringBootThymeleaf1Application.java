package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.service.IUploadFileService;

@SpringBootApplication
public class SpringBootThymeleaf1Application implements CommandLineRunner {
	
	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootThymeleaf1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		uploadFileService.deleteAll();
		uploadFileService.init();
		
		String pass = "12345";
		
		for (int i = 0; i < 2; i++) {
			String bcryptPassword = passwordEncoder.encode(pass);
			System.out.println(bcryptPassword);
		}
	}

}

