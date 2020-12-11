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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    void userExists_stExists__createPartage(){

        User user = new User("user", "pass");
        userRepository.save(user);
        SerieTemporelle st = new SerieTemporelle("title", "desc", user);
        stRepository.save(st);

        PartageRequest pr = new PartageRequest(user.getId(), st.getId(), "w");

        String uri = "http://localhost:" + port + "/partages";
        ResponseEntity<Void> responseEntity = testRestTemplate.postForEntity(uri, pr, Void.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        // check that it was actually created
        String location = responseEntity.getHeaders().getFirst("location");
        ResponseEntity<Partage> getResponse = testRestTemplate.getForEntity(location, Partage.class);
        Partage returned = getResponse.getBody();

        assertEquals(user, returned.getUser());
        assertEquals(st, returned.getSerieTemporelle());
        assertEquals("w", returned.getType());
    }


    @Test
    void userDoesNotExist__throwNotFoundException(){
        User user = new User("user", "pass");
        userRepository.save(user);
        SerieTemporelle st = new SerieTemporelle("title", "desc", user);
        stRepository.save(st);

        PartageRequest pr = new PartageRequest(5L, st.getId(), "w");

        String uri = "http://localhost:" + port + "/partages";
        ResponseEntity<ApiException> responseEntity = testRestTemplate.postForEntity(uri, pr, ApiException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void stDoesNotExist__throwNotFoundException(){
        User user = new User("user", "pass");
        userRepository.save(user);
        SerieTemporelle st = new SerieTemporelle("title", "desc", user);
        stRepository.save(st);

        PartageRequest pr = new PartageRequest(user.getId(), 5L, "w");

        String uri = "http://localhost:" + port + "/partages";
        ResponseEntity<ApiException> responseEntity = testRestTemplate.postForEntity(uri, pr, ApiException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
