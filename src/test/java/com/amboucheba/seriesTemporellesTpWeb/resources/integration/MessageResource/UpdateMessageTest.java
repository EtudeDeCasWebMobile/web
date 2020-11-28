package com.amboucheba.seriesTemporellesTpWeb.resources.integration.MessageResource;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = SeriesTemporellesTpWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SqlGroup({
        @Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UpdateMessageTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MessageRepository messageRepository;


    @Test
    void updateExistingMessage() throws Exception {
        // create a message and save it
        Message message = new Message( "username", "text");
        Message savedMessage = messageRepository.save(message);

        // create new message to update the first one
        String username = "User 1";
        String text = "This is the new text";
        Message newMessage = new Message( username, text);

        // update the first message with the second one
        // Prepare request
        String uri = "http://localhost:" + port + "/messages/" + savedMessage.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Message> entity = new HttpEntity<>(newMessage, headers);
        // Send request and get response
        ResponseEntity<Message> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, Message.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Message updatedMessage = response.getBody();

        assertEquals(savedMessage.getId(), updatedMessage.getId());
        assertEquals(username, updatedMessage.getUsername());
        assertEquals(text, updatedMessage.getText());
    }

    @Test
    // Message does not exist -> create it
    void messageDoesNotExist() throws Exception {
        //Create a message and save it
        Message oldMessage = messageRepository.save(new Message( "username", "text"));

        // delete the message
        messageRepository.deleteById(oldMessage.getId());

        // create new message to update
        String username = "User 1";
        String text = "Lorem ipsum...";
        Message updateMessage = new Message(username, text);

        // Prepare request
        String uri = "http://localhost:" + port + "/messages/" + oldMessage.getId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Message> entity = new HttpEntity<>(updateMessage, headers);
        // Send request and get response
        ResponseEntity<Message> response = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, Message.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().getFirst("location");

        Message createdMessage = testRestTemplate.getForObject(location, Message.class);

        assertEquals(username, createdMessage.getUsername());
        assertEquals(text, createdMessage.getText());

    }
}
