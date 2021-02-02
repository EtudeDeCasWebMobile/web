package com.amboucheba.etudeDeCasWeb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = EtudeDeCasWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EtudeDeCasWebApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	void contextLoads() {
		String uri = "http://localhost:" + port + "/";

		ResponseEntity<String> response = testRestTemplate.getForEntity(uri, String.class);

		String expected = "<html><body><h1>Welcome </h1><p>Serie Temporelle Service: Home page </p></body></html>";
		String received = response.getBody();

		assertEquals(expected, received);
	}

	@Test
	void withUserParam(){
		String user = "user";
		String uri = "http://localhost:" + port + "?user=" + user;

		ResponseEntity<String> response = testRestTemplate.getForEntity(uri, String.class);

		String expected = "<html><body><h1>Welcome " + user + "</h1><p>Serie Temporelle Service: Home page </p></body></html>";
		String received = response.getBody();

		assertEquals(expected, received);
	}

	@Test
	void withEmptyUserParam(){

		String uri = "http://localhost:" + port + "?user=";

		ResponseEntity<String> response = testRestTemplate.getForEntity(uri, String.class);

		String expected = "<html><body><h1>Welcome </h1><p>Serie Temporelle Service: Home page </p></body></html>";
		String received = response.getBody();

		assertEquals(expected, received);
	}

}
