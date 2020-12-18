package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.PartageController;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.ApiException;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.PartageRequest;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class DeletePartageTest {

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

    @Test
    void partageExists__removePartage(){

        User user = new User("user", "pass");
        userRepository.save(user);
        SerieTemporelle st = new SerieTemporelle("title", "desc", user);
        stRepository.save(st);

        Partage partage = new Partage(user, st, "w");
        partage = partageRepository.save(partage);


        String uri = "http://localhost:" + port + "/partages/" + partage.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Partage> entity = new HttpEntity<>( headers);
        // Send request and get response
        ResponseEntity<Partage> response = testRestTemplate.exchange(uri, HttpMethod.DELETE, entity, Partage.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<Partage> getResponse = testRestTemplate.getForEntity(uri, Partage.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void partageDoesNotExist__throwNotFoundException(){

        String uri = "http://localhost:" + port + "/partages/1";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Partage> entity = new HttpEntity<>( headers);
        // Send request and get response
        ResponseEntity<ApiException> response = testRestTemplate.exchange(uri, HttpMethod.DELETE, entity, ApiException.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
