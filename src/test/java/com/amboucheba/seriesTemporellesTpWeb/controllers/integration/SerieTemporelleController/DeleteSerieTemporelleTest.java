package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.SerieTemporelleController;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
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
public class DeleteSerieTemporelleTest {

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


    @Test
    void stExists__removeIt(){
        User user = new User("user", "pass");
        user = userRepository.save(user);
        SerieTemporelle st = new SerieTemporelle("t1", "d1", user);
        st = serieTemporelleRepository.save(st);

        String uri = "http://localhost:" + port + "/seriesTemporelles/" + st.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SerieTemporelle> entity = new HttpEntity<>( headers);
        // Send request and get response
        ResponseEntity<Void> response = testRestTemplate.exchange(uri, HttpMethod.DELETE, entity, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<SerieTemporelle> getResponse = testRestTemplate.getForEntity(uri, SerieTemporelle.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
