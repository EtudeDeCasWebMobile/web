package com.amboucheba.etudeDeCasWeb.Controllers.Integration.ToDelete.PartageController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Exceptions.ApiException;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.AuthInput;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Partage;
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
        @Sql(scripts = { "classpath:schema_tmp.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UpdatePartageTest {

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

        AuthInput authInput = new AuthInput("user", "pass");

        String uri = "http://localhost:" + port + "/authenticate";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authInput, Void.class);
        token = response.getHeaders().getFirst("token");
    }

    @Test
    void partageExists__updatePartageAndReturnIt(){

        Users users2 = new Users("user2", "pass");
        usersRepository.save(users2);
        SerieTemporelle st = new SerieTemporelle("title", "desc", users);
        stRepository.save(st);

        Partage partage = new Partage(users2, st, "w");
        partage = partageRepository.save(partage);

        Partage newPartage = new Partage(users2, st, "r");

        String uri = "http://localhost:" + port + "/partages/" + partage.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Partage> entity = new HttpEntity<>(newPartage, headers);
        // Send request and get response
        ResponseEntity<Partage> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, Partage.class);
        Partage returned = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users2.getUsername(), returned.getUser().getUsername());
        assertEquals(st, returned.getSerieTemporelle());
        assertEquals("r", returned.getType());
    }

    @Test
    void partageDoesNotExist__throwNotFoundException(){

        Partage newPartage = new Partage(null, null, "r");

        String uri = "http://localhost:" + port + "/partages/7" ;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Partage> entity = new HttpEntity<>(newPartage, headers);
        // Send request and get response
        ResponseEntity<ApiException> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, ApiException.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
