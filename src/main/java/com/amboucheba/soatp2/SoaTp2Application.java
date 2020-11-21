package com.amboucheba.soatp2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@RestController
@EnableSwagger2
public class SoaTp2Application {

	public static void main(String[] args) {
		SpringApplication.run(SoaTp2Application.class, args);
	}

	@GetMapping(produces = "text/html")
	public String home(){
		return "<html><body><p>Message Service: Home page </p></body></html>";
	}



}
