package com.amboucheba.etudeDeCasWeb.Controllers.Integration.CollectionController;


import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Models.CollectionsMixin;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.AuthInput;
import com.amboucheba.etudeDeCasWeb.Models.Outputs.Collections;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Repositories.CollectionRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = EtudeDeCasWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetUserCollectionsTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user;
    String token;

    @BeforeEach
    void setAuthHeader(){

        user = new User( "user@gmail.com", passwordEncoder.encode("password"));
        user = userRepository.save(user);

        AuthInput authInput = new AuthInput(user.getEmail(), "password");

        String uri = "http://localhost:" + port + "/auth";
        ResponseEntity<String> response = testRestTemplate.postForEntity(uri, authInput, String.class);

        token = response.getHeaders().getFirst("AuthToken");
    }

    @AfterEach
    void clean(){
        collectionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void userExists__returnHisCollections() throws Exception {

        Collection c1 = new Collection("t1", user);
        Collection c2 = new Collection("t2", user);
        collectionRepository.save(c1);
        collectionRepository.save(c2);

        String uri = "http://localhost:" + port + "/me/collections";

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>( headers);
        // Send request and get response
        ResponseEntity<CollectionsMixin> response = testRestTemplate.exchange(uri, HttpMethod.GET, entity, CollectionsMixin.class);

        CollectionsMixin collections = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, collections.getCollections().size());
    }

    @Test
    void userDoesNotExists__returnNotFound() throws Exception {

        userRepository.deleteById(user.getId());

        String uri = "http://localhost:" + port + "/me/collections";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>( headers);
        // Send request and get response
        ResponseEntity<Void> response = testRestTemplate.exchange(uri, HttpMethod.GET, entity, Void.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
