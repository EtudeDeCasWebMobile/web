package com.amboucheba.soatp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@RestController
@EnableSwagger2
public class SoaTp2Application {

	public static void main(String[] args) {
		SpringApplication.run(SoaTp2Application.class, args);
	}

	@GetMapping(produces = "text/html")
	public String home(@RequestParam(value = "user", required = false, defaultValue = "") String user){
		return "<html><body><h1>Welcome " + user + "</h1><p>Message Service: Home page </p></body></html>";
	}



}
