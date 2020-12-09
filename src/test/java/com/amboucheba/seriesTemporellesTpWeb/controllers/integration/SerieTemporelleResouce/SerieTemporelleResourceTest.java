package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.SerieTemporelleResouce;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.SerieTemplorelleList;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.UserList;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class SerieTemporelleResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;


    //@Test
    void findSeriesTemporellesTest() throws Exception {
        String uri = "http://localhost:" + port + "/seriestemporelles";
        ResponseEntity<SerieTemplorelleList> response = testRestTemplate.getForEntity(uri, SerieTemplorelleList.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().getCount());
    }

    //@Test
    void findSerieTemporelleByIdTest() throws Exception {
        String uri = "http://localhost:" + port + "/seriestemporelles/1";
        ResponseEntity<SerieTemporelle> response = testRestTemplate.getForEntity(uri, SerieTemporelle.class);
        System.out.println(response.getBody().toString());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(response.getBody().getTitre(), "titre1");
        assertEquals(response.getBody().getDescription(), "description1");
        assertEquals(response.getBody().getId(), 1);
    }


    @Test
    void createSerieTemporelleTest() throws Exception {
        String titre = "titre4";
        String description = "description4";
        SerieTemporelle serieTemporelle = new SerieTemporelle(titre, description);

        String uri = "http://localhost:" + port + "/users/1/seriestemporelles";

        ResponseEntity<SerieTemporelle> response = testRestTemplate.postForEntity(uri, serieTemporelle, SerieTemporelle.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().getFirst("location");
        SerieTemporelle serieTemporelleSaved = testRestTemplate.getForObject(location, SerieTemporelle.class);

        assertNotNull(serieTemporelleSaved.getId());
        assertEquals(titre, serieTemporelleSaved.getTitre());
        assertEquals(description, serieTemporelleSaved.getDescription());
    }

}