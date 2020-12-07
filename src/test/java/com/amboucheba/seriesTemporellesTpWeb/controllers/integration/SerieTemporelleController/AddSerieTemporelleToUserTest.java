package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.SerieTemporelleController;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.ApiException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Test
    void userExists__returnStLocationInHeader() throws Exception {
        User user = new User("user", "pass");
        user = userRepository.save(user);

        SerieTemporelle st = new SerieTemporelle("title", "desc");

        String uri = "http://localhost:" + port + "/users/" + user.getId() + "/seriesTemporelles";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, st, Void.class );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check that it was actually created
        String location = response.getHeaders().getFirst("location");
        ResponseEntity<SerieTemporelle> getResponse = testRestTemplate.getForEntity(location, SerieTemporelle.class);
        SerieTemporelle returned = getResponse.getBody();

        assertEquals("title", returned.getTitre());
        assertEquals("desc", returned.getDescription());

    }

    @Test
    void userDoesNotExist__throwNotFoundException(){
        SerieTemporelle st = new SerieTemporelle("title", "desc");

        String uri = "http://localhost:" + port + "/users/1/seriesTemporelles";
        ResponseEntity<ApiException> responseEntity = testRestTemplate.postForEntity(uri, st, ApiException.class);
        ApiException e = responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
