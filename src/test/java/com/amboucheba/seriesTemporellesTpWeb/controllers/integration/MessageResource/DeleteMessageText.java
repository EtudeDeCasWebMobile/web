package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.MessageResource;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import com.amboucheba.seriesTemporellesTpWeb.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class DeleteMessageText {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void messageExists() throws Exception {
        // create a message and save it
        Message message = new Message( "username", "text");
        Message savedMessage = messageRepository.save(message);

        // Prepare request
        String uri = "http://localhost:" + port + "/messages/" + savedMessage.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Message> entity = new HttpEntity<>( headers);
        // Send request and get response
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);

        // Http status must be OK(200)
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        // Message must have benn deleted
        Optional<Message> messageResult = messageRepository.findById(savedMessage.getId());
        assertTrue(messageResult.isEmpty());
    }

    @Test
    void messageDoesNotExist() throws Exception {
        // create a message and save it
        Message message = new Message( "username", "text");
        Message savedMessage = messageRepository.save(message);

        // delete the message
        messageRepository.deleteById(savedMessage.getId());

        // Prepare request
        String uri = "http://localhost:" + port + "/messages/" + savedMessage.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Message> entity = new HttpEntity<>( headers);
        // Send request and get response
        ResponseEntity<String> response = testRestTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);

        // Http status must be NOT_FOUND(404)
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

}
