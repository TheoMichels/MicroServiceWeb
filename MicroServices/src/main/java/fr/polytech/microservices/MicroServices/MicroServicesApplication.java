package fr.polytech.microservices.MicroServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class MicroServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroServicesApplication.class, args);
		ProfileController profileController = new ProfileController();
		profileController.checkTokenAgainstUser("dkjqshkdhqkd", 12L);
	}

}
