package com.amboucheba.soatp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SoaTp2Application {

	public static void main(String[] args) {
		SpringApplication.run(SoaTp2Application.class, args);
	}

	@GetMapping(produces = "text/html")
	public String home(){
		return "<html><body><p> Home page </p></body></html>";
	}



}
