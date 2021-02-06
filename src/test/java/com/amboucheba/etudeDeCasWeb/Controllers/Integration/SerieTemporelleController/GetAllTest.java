package com.amboucheba.etudeDeCasWeb.Controllers.Integration.SerieTemporelleController;


import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.SerieTemporelleRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EtudeDeCasWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema121.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class GetAllTest {

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

    //TO delete
//    @Test
//    void __returnSts() throws Exception {
//        User user = new User("user", "pass");
//        user = userRepository.save(user);
//        SerieTemporelle st1 = new SerieTemporelle("t1", "d1", user);
//        SerieTemporelle st2 = new SerieTemporelle("t2", "d2", user);
//        serieTemporelleRepository.save(st1);
//        serieTemporelleRepository.save(st2);
//
//        String uri = "http://localhost:" + port + "/seriesTemporelles";
//        ResponseEntity<SerieTemplorelleList> responseEntity = testRestTemplate.getForEntity(uri, SerieTemplorelleList.class);
//        SerieTemplorelleList stList = responseEntity.getBody();
//
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(2, stList.getCount() );
//    }
}
