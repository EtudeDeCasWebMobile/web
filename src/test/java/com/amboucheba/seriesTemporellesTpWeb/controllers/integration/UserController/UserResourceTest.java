package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.UserController;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.ApiException;
import com.amboucheba.seriesTemporellesTpWeb.models.AuthenticationRequest;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.UserList;
import com.amboucheba.seriesTemporellesTpWeb.models.RegisterUserInput;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    User user;
    String token;

    @BeforeEach
    void setAuthHeader(){
        user = new User("user", passwordEncoder.encode("pass"));
        user = userRepository.save(user);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest("user", "pass");

        String uri = "http://localhost:" + port + "/authenticate";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authenticationRequest, Void.class);
        token = response.getHeaders().getFirst("token");
    }

    //To delete
//    @Test
//    void getAllTest() throws Exception {
//
//        // create users
//        userRepository.save(new User("user1", "pass"));
//        userRepository.save(new User("user2", "pass"));
//
//        // get users through /users endpoint
//        String uri = "http://localhost:" + port + "/users/";
//        ResponseEntity<UserList> response = testRestTemplate.getForEntity(uri, UserList.class);
//        UserList userList = response.getBody();
//
//        // assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(userList.getCount(), 2);
//    }

    @Test
    void getUserByIdTest() throws Exception {


        String uri = "http://localhost:" + port + "/users/" + user.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        // Send request and get response
        ResponseEntity<User> response = testRestTemplate.exchange(uri, HttpMethod.GET, entity, User.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());

        User returned = response.getBody();

        assertEquals(user.getUsername(), returned.getUsername());
    }

    @Test
    void addUserTest() throws Exception {
        String username = "user3p";
        String password = "password3";
        RegisterUserInput input = new RegisterUserInput(username, password);
        User user = new User(username, password);

        String uri = "http://localhost:" + port + "/users";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, input, Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

//        String location = response.getHeaders().getFirst("location");
//        User savedUser = testRestTemplate.getForObject(location, User.class);
//
//        assertNotNull(savedUser.getId());
//        assertEquals(username, savedUser.getUsername());
    }

}