package com.amboucheba.etudeDeCasWeb.Controllers.Integration.UserController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.AuthInput;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.RegisterInput;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = EtudeDeCasWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SqlGroup({
//        @Sql(scripts = { "classpath:schema121.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
//        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//})
class UsersResourceTest {

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
        user = new User("email@salut.com", "user", passwordEncoder.encode("pass"));
        user = userRepository.save(user);

        AuthInput authInput = new AuthInput("user", "pass");

        String uri = "http://localhost:" + port + "/auth";
        ResponseEntity<Void> response = testRestTemplate.postForEntity(uri, authInput, Void.class);
        token = response.getHeaders().getFirst("AuthToken");
    }

    @Test
    void getUserMeTest() throws Exception {


        String uri = "http://localhost:" + port + "/users/me" ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        // Send request and get response
        ResponseEntity<User> response = testRestTemplate.exchange(uri, HttpMethod.GET, entity, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        User returned = response.getBody();

        assertEquals(user.getUsername(), returned.getUsername());
        assertEquals(user.getEmail(), returned.getEmail());
    }

    @Test
    void addUserTest() throws Exception {
        String username = "user3p";
        String password = "password3";
        String email = "email@salut2.com";
        RegisterInput input = new RegisterInput(email, username, password);
        Users users = new Users(username, password);

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