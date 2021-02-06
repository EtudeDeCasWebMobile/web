package com.amboucheba.etudeDeCasWeb.Controllers.Integration.UserController;

import com.amboucheba.etudeDeCasWeb.EtudeDeCasWebApplication;
import com.amboucheba.etudeDeCasWeb.Models.AuthenticationRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.RegisterUserInput;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = EtudeDeCasWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema121.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UsersResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

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


        String uri = "http://localhost:" + port + "/users/" + users.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        // Send request and get response
        ResponseEntity<Users> response = testRestTemplate.exchange(uri, HttpMethod.GET, entity, Users.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());

        Users returned = response.getBody();

        assertEquals(users.getUsername(), returned.getUsername());
    }

    @Test
    void addUserTest() throws Exception {
        String username = "user3p";
        String password = "password3";
        RegisterUserInput input = new RegisterUserInput(username, password);
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