package com.amboucheba.etudeDeCasWeb.Controllers.Integration.CollectionController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.AuthInput;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.CollectionInput;
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
public class UpdateCollectionTest {

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

        AuthInput authInput = new AuthInput("user@gmail.com", "password");

        String uri = "http://localhost:" + port + "/auth";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authInput, Void.class);
        token = response.getHeaders().getFirst("AuthToken");
    }

    @AfterEach
    void clean(){
        collectionRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void collectionDoesNotExist__returnNotFound() throws Exception{

        String tag = "tag";
        CollectionInput collectionInput = new CollectionInput(tag);

        String uri = "http://localhost:" + port + "/collections/1" ;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<CollectionInput> entity = new HttpEntity<>( collectionInput, headers);
        // Send request and get response
        ResponseEntity<Void> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

//    @Test
//    void userIsOwner__returnUpdatedCollection() throws Exception{
//        String tag = "tag";
//        Collection collection = new Collection(tag, user);
//        collectionRepository.save(collection);
//
//        CollectionInput collectionInput = new CollectionInput("new tag");
//
//        String uri = "http://localhost:" + port + "/collections/" + collection.getId() ;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token);
//        HttpEntity<CollectionInput> entity = new HttpEntity<>( collectionInput, headers);
//        // Send request and get response
//        ResponseEntity<Collection> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, Collection.class);
//        Collection returned = response.getBody();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals("new tag", returned.getTag());
//    }

    @Test
    void tagTooLong__returnBadRequest() throws Exception {
        Collection collection = new Collection("tag", user);
        collectionRepository.save(collection);
        String tag = "a".repeat(51);

        CollectionInput collectionInput = new CollectionInput(tag);

        String uri = "http://localhost:" + port + "/collections/" + collection.getId() ;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<CollectionInput> entity = new HttpEntity<>( collectionInput, headers);
        // Send request and get response
        ResponseEntity<Void> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void emptyTag__returnBadRequest() throws Exception {

        Collection collection = new Collection("tag", user);
        collectionRepository.save(collection);

        String tag = "";
        CollectionInput collectionInput = new CollectionInput(tag);

        String uri = "http://localhost:" + port + "/collections/" + collection.getId() ;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<CollectionInput> entity = new HttpEntity<>( collectionInput, headers);
        // Send request and get response
        ResponseEntity<Void> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
