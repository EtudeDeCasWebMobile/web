package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.UserResource;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.UserList;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql", "classpath:data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void findUsersTest() throws Exception {
        String uri = "http://localhost:" + port + "/users/";
        ResponseEntity<UserList> response = testRestTemplate.getForEntity(uri, UserList.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody().getCount());
        assertEquals(response.getBody().getCount(), 2);
    }

    @Test
    void findUserByIdTest() throws Exception {
        String uri = "http://localhost:" + port + "/users/1";
        ResponseEntity<User> response = testRestTemplate.getForEntity(uri, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(response.getBody().getUsername(), "user1");
        assertEquals(response.getBody().getPassword(), "password1");
        assertEquals(response.getBody().getId(), 1);
    }


    @Test
    void createUserTest() throws Exception {
        String username = "user3";
        String password = "password3";
        User user = new User(username, password);

        String uri = "http://localhost:" + port + "/users/";

        ResponseEntity<User> response = testRestTemplate.postForEntity(uri, user, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().getFirst("location");
        User savedUser = testRestTemplate.getForObject(location, User.class);

        assertNotNull(savedUser.getId());
        assertEquals(username, savedUser.getUsername());
        assertEquals(password, savedUser.getPassword());
    }

    @Test
    void duplicateUserTest() throws Exception {
        String username = "user1";
        String password = "password3";
        User user = new User(username, password);

        String uri = "http://localhost:" + port + "/users/";

        ResponseEntity<User> response = testRestTemplate.postForEntity(uri, user, User.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }
}