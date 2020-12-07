package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.PartageController;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.ApiException;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.PartageList;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
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
public class GetAllByUserIdTest {

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
    void userExists__returnPartagesOfUser() throws Exception {
        User user = new User("user", "pass");
        user = userRepository.save(user);
        SerieTemporelle st = new SerieTemporelle("title", "desc", user);
        stRepository.save(st);
        Partage partage = new Partage(user, st, "w");
        partage = partageRepository.save(partage);

        String uri = "http://localhost:" + port + "/users/" + user.getId() + "/partages" ;
        ResponseEntity<PartageList> responseEntity = testRestTemplate.getForEntity(uri, PartageList.class);
        PartageList returned = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, returned.getCount() );
        assertEquals(partage, returned.getPartages().get(0));
    }

    @Test
    void userDoesNotExist__throwNotFoundException(){

        String uri = "http://localhost:" + port + "/users/1/partages";
        ResponseEntity<ApiException> responseEntity = testRestTemplate.getForEntity(uri, ApiException.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
