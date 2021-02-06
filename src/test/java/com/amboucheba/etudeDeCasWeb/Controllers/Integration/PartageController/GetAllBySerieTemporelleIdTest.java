package com.amboucheba.etudeDeCasWeb.Controllers.Integration.PartageController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Exceptions.ApiException;
import com.amboucheba.etudeDeCasWeb.Models.AuthenticationRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists.PartageList;
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
        @Sql(scripts = { "classpath:schema121.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class GetAllBySerieTemporelleIdTest {

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

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "pass");

        String uri = "http://localhost:" + port + "/authenticate";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authenticationRequest, Void.class);
        token = response.getHeaders().getFirst("token");
    }

    @Test
    void stExists__returnPartagesOfSt() throws Exception {
        Users users2 = new Users("user2", "pass");
        users2 = usersRepository.save(users2);
        SerieTemporelle st = new SerieTemporelle("title", "desc", users);
        stRepository.save(st);
        Partage partage = new Partage(users2, st, "w");
        partage = partageRepository.save(partage);

        String uri = "http://localhost:" + port + "/seriesTemporelles/" + st.getId() + "/partages" ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<SerieTemporelle> entity = new HttpEntity<>(headers);
        // Send request and get response
        ResponseEntity<PartageList> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, entity, PartageList.class);

        PartageList returned = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, returned.getCount() );
        assertEquals(partage, returned.getPartages().get(0));
    }

    @Test
    void stDoesNotExist__throwNotFoundException(){

        String uri = "http://localhost:" + port + "/seriesTemporelles/1/partages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<SerieTemporelle> entity = new HttpEntity<>(headers);
        // Send request and get response
        ResponseEntity<ApiException> responseEntity = testRestTemplate.exchange(uri, HttpMethod.GET, entity, ApiException.class);


        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
