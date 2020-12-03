package com.amboucheba.seriesTemporellesTpWeb.controllers.unit.MessageResource;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.ApiException;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import com.amboucheba.seriesTemporellesTpWeb.repositories.MessageRepository;
import com.amboucheba.seriesTemporellesTpWeb.controllers.MessageResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageResource.class)
class GetMessageByIdTest {

    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // Get message with valid id : expect a message
    void getMessageById() throws Exception {

        long messageId = 1;
        Message message = new Message(messageId, "User 1", "Message 1", new Date());

        Mockito.when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.get("/messages/" + messageId);
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString( message);

        // Compare expected response with actual response
        assertEquals(expectedResponse, response_str);
    }

    @Test
    void messageIdNotFound() throws Exception {
        long messageId = 1;

        Mockito.when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.get("/messages/" + messageId);
        MvcResult response = mvc.perform(request).andReturn();
        // Response is supposed to be an ApiException
        String response_str = response.getResponse().getContentAsString();
        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.NOT_FOUND);

    }
}