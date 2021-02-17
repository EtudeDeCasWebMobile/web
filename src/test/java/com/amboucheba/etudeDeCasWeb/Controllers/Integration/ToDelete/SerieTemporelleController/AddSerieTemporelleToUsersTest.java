package com.amboucheba.etudeDeCasWeb.Controllers.Integration.ToDelete.SerieTemporelleController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.AuthInput;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = EtudeDeCasWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema121.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class AddSerieTemporelleToUsersTest {

    @LocalServerPort
    private int port;

    @Autowired
    private SerieTemporelleRepository serieTemporelleRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Users users;
    String token;

    @BeforeEach
    void setAuthHeader(){
        users = new Users("user", passwordEncoder.encode("pass"));
        users = usersRepository.save(users);

        AuthInput authInput = new AuthInput("user", "pass");

        String uri = "http://localhost:" + port + "/authenticate";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authInput, Void.class);
        token = response.getHeaders().getFirst("token");
    }

    @Test
    void userExists__returnStLocationInHeader() throws Exception {

        SerieTemporelle st = new SerieTemporelle("title", "desc");

        String uri = "http://localhost:" + port + "/users/" + users.getId() + "/seriesTemporelles";

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
