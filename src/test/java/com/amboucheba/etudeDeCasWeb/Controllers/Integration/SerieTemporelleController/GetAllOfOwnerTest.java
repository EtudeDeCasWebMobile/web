package com.amboucheba.etudeDeCasWeb.Controllers.Integration.SerieTemporelleController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Models.AuthenticationRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists.SerieTemplorelleList;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.SerieTemporelleRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
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
        @Sql(scripts = { "classpath:schema121.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class GetAllOfOwnerTest {

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

    @Autowired
    PasswordEncoder passwordEncoder;

    Users users;
    String token;

    @BeforeEach
    void setAuthHeader(){
        users = new Users("user", passwordEncoder.encode("pass"));
        users = userRepository.save(users);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "pass");

        String uri = "http://localhost:" + port + "/authenticate";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authenticationRequest, Void.class);
        token = response.getHeaders().getFirst("token");
    }

    @Test
    void userExists__returnStsOfUser() throws Exception {

        SerieTemporelle st1 = new SerieTemporelle("t1", "d1", users);
        SerieTemporelle st2 = new SerieTemporelle("t2", "d2", users);
        serieTemporelleRepository.save(st1);
        serieTemporelleRepository.save(st2);

        String uri = "http://localhost:" + port + "/users/" + users.getId() +"/seriesTemporelles";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<SerieTemporelle> entity = new HttpEntity<>(headers);
        // Send request and get response
        ResponseEntity<SerieTemplorelleList> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, entity, SerieTemplorelleList.class);

        SerieTemplorelleList stList = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, stList.getCount() );
    }

//    @Test
//    void userDoesNotExist__throwNotFoundException(){
//
//        String uri = "http://localhost:" + port + "/users/1/seriesTemporelles";
//        ResponseEntity<ApiException> responseEntity = testRestTemplate.getForEntity(uri, ApiException.class);
//        ApiException e = responseEntity.getBody();
//
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//    }
}
