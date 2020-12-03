package com.amboucheba.seriesTemporellesTpWeb.controllers.unit.MessageResource;

import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import com.amboucheba.seriesTemporellesTpWeb.models.MessageList;
import com.amboucheba.seriesTemporellesTpWeb.repositories.MessageRepository;
import com.amboucheba.seriesTemporellesTpWeb.controllers.MessageResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageResource.class)
class GetAllTest {

    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllWithoutUsername() throws Exception {

        // Mock dependency response: messageRepository
        List<Message> messages = Arrays.asList(
                new Message("u1", "t1"),
                new Message("u2", "t2")
        );
        Mockito.when(messageRepository.findAll()).thenReturn(messages);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.get("/messages");
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(new MessageList( messages));

        // Compare expected response with actual response
        assertEquals(expectedResponse, response_str);
    }

    @Test
    void getByUsername() throws Exception {

        // Mock dependency response: messageRepository
        String username = "u1";
        List<Message> messages = Arrays.asList(
                new Message(username, "t1"),
                new Message(username, "t2")
        );
        Mockito.when(messageRepository.findByUsername(username)).thenReturn(messages);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.get("/messages?username="+username);
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(new MessageList( messages));

        // Compare expected response with actual response
        assertEquals(expectedResponse, response_str);
    }

    @Test
    //Should get all messages
    void usernameEmpty() throws Exception {

        // Mock dependency response: messageRepository
        // username empty
        String username = "";
        List<Message> messages = Arrays.asList(
                new Message(username, "t1"),
                new Message(username, "t2")
        );
        Mockito.when(messageRepository.findAll()).thenReturn(messages);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.get("/messages?username="+username);
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(new MessageList( messages));

        // Compare expected response with actual response
        assertEquals(expectedResponse, response_str);
    }
}