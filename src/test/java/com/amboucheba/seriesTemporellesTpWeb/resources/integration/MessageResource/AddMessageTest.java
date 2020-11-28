package com.amboucheba.seriesTemporellesTpWeb.resources.integration.MessageResource;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class AddMessageTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void addMessage() throws Exception {
        String username = "User 1";
        String text = "This is some text";
        Message message = new Message(username, text);

        String uri = "http://localhost:" + port + "/messages";

        ResponseEntity<Message> response = testRestTemplate.postForEntity(uri, message, Message.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().getFirst("location");
        Message savedMessage = testRestTemplate.getForObject(location, Message.class);

        assertNotNull(savedMessage.getId());
        assertNotNull(savedMessage.getCreated_at());
        assertEquals(username, savedMessage.getUsername());
        assertEquals(text, savedMessage.getText());
    }
}