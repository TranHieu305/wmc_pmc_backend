package com.wms.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WmsApplication {
	@RequestMapping("/")
	String home() {
		return "Server is running";
	}
	public static void main(String[] args) {
		System.out.println("------------------rerun");
		SpringApplication.run(WmsApplication.class, args);

	}

}
