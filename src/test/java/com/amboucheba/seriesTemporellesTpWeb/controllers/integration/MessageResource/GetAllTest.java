package com.amboucheba.seriesTemporellesTpWeb.controllers.integration.MessageResource;

import com.amboucheba.seriesTemporellesTpWeb.SeriesTemporellesTpWebApplication;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.MessageList;
import com.amboucheba.seriesTemporellesTpWeb.repositories.MessageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        @Sql(scripts = { "classpath:schema.sql", "classpath:data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = { "classpath:reset.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class GetAllTest {

    @LocalServerPort
    private int port;

    @Autowired
    private  MessageRepository messageRepository;

    @Autowired
    private  TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll() {
       String uri = "http://localhost:" + port + "/messages";
       ResponseEntity<MessageList> responseEntity = testRestTemplate.getForEntity(uri, MessageList.class);
       MessageList messageList = responseEntity.getBody();

       assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, messageList.getCount() );
    }

    @Test
    void getByUsername() throws Exception {
        String username = "AB";
        String uri = "http://localhost:" + port + "/messages?username="+ username;
        ResponseEntity<MessageList> responseEntity = testRestTemplate.getForEntity(uri, MessageList.class);
        MessageList messageList = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, messageList.getCount() );
    }
}