package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.SerieTemporelleController;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.ApiException;
import com.amboucheba.seriesTemporellesTpWeb.models.AuthenticationRequest;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.SerieTemplorelleList;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class AddSerieTemporelleToUserTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SerieTemporelleRepository serieTemporelleRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

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
    void userExists__returnStLocationInHeader() throws Exception {

        SerieTemporelle st = new SerieTemporelle("title", "desc");

        String uri = "http://localhost:" + port + "/users/" + user.getId() + "/seriesTemporelles";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<SerieTemporelle> entity = new HttpEntity<>(st, headers);
        // Send request and get response
        ResponseEntity<Void> response = testRestTemplate.exchange(uri, HttpMethod.POST, entity, Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

//    @Test
//    void userDoesNotExist__throwNotFoundException(){
//
//        userRepository.deleteById(user.getId());
//        SerieTemporelle st = new SerieTemporelle("title", "desc");
//
//        String uri = "http://localhost:" + port + "/users/" + user.getId() + "/seriesTemporelles";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(token);
//        HttpEntity<SerieTemporelle> entity = new HttpEntity<>(st, headers);
//        // Send request and get response
//        ResponseEntity<ApiException> response = testRestTemplate.exchange(uri, HttpMethod.POST, entity, ApiException.class);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
}
