package com.amboucheba.soatp2.resources.unit.MessageResource;

import com.amboucheba.soatp2.models.Message;
import com.amboucheba.soatp2.models.MessageList;
import com.amboucheba.soatp2.repositories.MessageRepository;
import com.amboucheba.soatp2.resources.MessageResource;
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

import java.util.ArrayList;
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
    void getAll() throws Exception {

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
}