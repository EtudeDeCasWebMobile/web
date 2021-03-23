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

@SpringBootTest(classes = EtudeDeCasWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema_tmp.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UpdateSerieTemporelleTest {

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
    void stExists__returnUpdatedSt() throws Exception {

        SerieTemporelle st = new SerieTemporelle("t1", "d1", users);
        st = serieTemporelleRepository.save(st);

        SerieTemporelle newst = new SerieTemporelle("new title", "new desc");

        String uri = "http://localhost:" + port + "/seriesTemporelles/" + st.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<SerieTemporelle> entity = new HttpEntity<>(newst, headers);
        // Send request and get response
        ResponseEntity<SerieTemporelle> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, SerieTemporelle.class);
        SerieTemporelle returned = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(st.getId(), returned.getId());
        assertEquals(newst.getTitre(), returned.getTitre());
        assertEquals(newst.getDescription(), returned.getDescription());

    }

    @Test
    void stDoesNotExist__throwNotFoundException(){

        SerieTemporelle st = new SerieTemporelle("new title", "new desc");

        String uri = "http://localhost:" + port + "/seriesTemporelles/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<SerieTemporelle> entity = new HttpEntity<>(st, headers);
        // Send request and get response
        ResponseEntity<SerieTemporelle> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, SerieTemporelle.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
