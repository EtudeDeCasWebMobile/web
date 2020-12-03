package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.MessageResource;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import com.amboucheba.seriesTemporellesTpWeb.repositories.MessageRepository;
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
class GetMessageByIdTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MessageRepository messageRepository;

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    void getExistingMessageById() throws Exception {
        String username = "New User";
        String text = "New Message!!";
        Message messageToSave = new Message(username, text);
        Message savedMessage = messageRepository.save(messageToSave);

        String uri = "http://localhost:" + port + "/messages/" + savedMessage.getId();

        ResponseEntity<Message> response = testRestTemplate.getForEntity(uri, Message.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Message fetchedMessage = response.getBody();

        assertEquals(username, fetchedMessage.getUsername());
        assertEquals(text, fetchedMessage.getText());
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    void messageDoesNotExist() throws Exception {
        // Create a message and save it
        Message message = new Message("User 1", "text");
        Message savedMessage = messageRepository.save(message);

        // Delete the created message
        messageRepository.deleteById(savedMessage.getId());

        // try to get the deleted message
        String uri = "http://localhost:" + port + "/messages/" + savedMessage.getId();

        ResponseEntity<Message> response = testRestTemplate.getForEntity(uri, Message.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}