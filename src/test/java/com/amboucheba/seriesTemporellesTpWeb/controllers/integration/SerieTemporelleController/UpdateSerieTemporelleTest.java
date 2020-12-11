package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.SerieTemporelleController;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.ApiException;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.SerieTemplorelleList;
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
    UserRepository userRepository;

    @Test
    void stExists__returnUpdatedSt() throws Exception {
        User user = new User("user", "pass");
        user = userRepository.save(user);
        SerieTemporelle st = new SerieTemporelle("t1", "d1", user);
        st = serieTemporelleRepository.save(st);


        SerieTemporelle newst = new SerieTemporelle("new title", "new desc");

        String uri = "http://localhost:" + port + "/seriesTemporelles/" + st.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
        HttpEntity<SerieTemporelle> entity = new HttpEntity<>(st, headers);
        // Send request and get response
        ResponseEntity<SerieTemporelle> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, SerieTemporelle.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
