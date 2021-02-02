package com.amboucheba.etudeDeCasWeb.Controllers.Integration.PartageController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Exceptions.ApiException;
import com.amboucheba.etudeDeCasWeb.Models.*;
import com.amboucheba.etudeDeCasWeb.Repositories.PartageRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.SerieTemporelleRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EtudeDeCasWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class AddPartageTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PartageRepository partageRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SerieTemporelleRepository stRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user;
    String token;

    @BeforeEach
    void setAuthHeader(){
        user = new User("user", passwordEncoder.encode("pass"));
        user = userRepository.save(user);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "pass");

        String uri = "http://localhost:" + port + "/authenticate";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authenticationRequest, Void.class);
        token = response.getHeaders().getFirst("token");
    }

    @Test
    void userExists_stExists__createPartage(){

        User user2 = new User("user2", "pass");
        userRepository.save(user2);
        SerieTemporelle st = new SerieTemporelle("title", "desc", user);
        stRepository.save(st);

        PartageRequest pr = new PartageRequest(user2.getId(), st.getId(), "w");

        String uri = "http://localhost:" + port + "/partages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<PartageRequest> entity = new HttpEntity<>(pr, headers);
        // Send request and get response
        ResponseEntity<Void> responseEntity = testRestTemplate.exchange(uri, HttpMethod.POST, entity, Void.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }


    @Test
    void userDoesNotExist__throwNotFoundException(){

        SerieTemporelle st = new SerieTemporelle("title", "desc", user);
        stRepository.save(st);

        PartageRequest pr = new PartageRequest(5L, st.getId(), "w");

        String uri = "http://localhost:" + port + "/partages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<PartageRequest> entity = new HttpEntity<>(pr, headers);
        // Send request and get response
        ResponseEntity<ApiException> responseEntity = testRestTemplate.exchange(uri, HttpMethod.POST, entity, ApiException.class);


        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void stDoesNotExist__throwNotFoundException(){

        User user2 = new User("user2", "pass");
        userRepository.save(user2);

        PartageRequest pr = new PartageRequest(user2.getId(), 5L, "w");

        String uri = "http://localhost:" + port + "/partages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<PartageRequest> entity = new HttpEntity<>(pr, headers);
        // Send request and get response
        ResponseEntity<ApiException> responseEntity = testRestTemplate.exchange(uri, HttpMethod.POST, entity, ApiException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
