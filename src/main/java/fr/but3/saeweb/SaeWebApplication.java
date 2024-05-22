package fr.but3.saeweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SaeWebApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SaeWebApplication.class, args);
	}

}
