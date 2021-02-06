package com.amboucheba.etudeDeCasWeb.Controllers.Integration.PartageController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Exceptions.ApiException;
import com.amboucheba.etudeDeCasWeb.Models.*;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.PartageRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.PartageRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.SerieTemporelleRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
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
        @Sql(scripts = { "classpath:schema121.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
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
    UsersRepository usersRepository;

    @Autowired
    SerieTemporelleRepository stRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Users users;
    String token;

    @BeforeEach
    void setAuthHeader(){
        users = new Users("user", passwordEncoder.encode("pass"));
        users = usersRepository.save(users);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "pass");

        String uri = "http://localhost:" + port + "/authenticate";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authenticationRequest, Void.class);
        token = response.getHeaders().getFirst("token");
    }

    @Test
    void userExists_stExists__createPartage(){

        Users users2 = new Users("user2", "pass");
        usersRepository.save(users2);
        SerieTemporelle st = new SerieTemporelle("title", "desc", users);
        stRepository.save(st);

        PartageRequest pr = new PartageRequest(users2.getId(), st.getId(), "w");

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

        SerieTemporelle st = new SerieTemporelle("title", "desc", users);
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

        Users users2 = new Users("user2", "pass");
        usersRepository.save(users2);

        PartageRequest pr = new PartageRequest(users2.getId(), 5L, "w");

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
