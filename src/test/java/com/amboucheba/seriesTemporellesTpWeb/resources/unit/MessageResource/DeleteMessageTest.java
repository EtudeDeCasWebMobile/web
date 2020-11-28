package com.amboucheba.seriesTemporellesTpWeb.resources.unit.MessageResource;

import com.amboucheba.seriesTemporellesTpWeb.repositories.MessageRepository;
import com.amboucheba.seriesTemporellesTpWeb.resources.MessageResource;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageResource.class)
class DeleteMessageTest {

    @MockBean
    MessageRepository messageRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deleteExistingMessage() throws Exception {
        Long messageId = 1L;
        Mockito.when(messageRepository.existsById(messageId)).thenReturn(true);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.delete("/messages/" + messageId );
        MvcResult response = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getResponse().getStatus());
    }

    @Test
    void messageDoesNotExist() throws Exception {
        Long messageId = 1L;
        Mockito.when(messageRepository.existsById(messageId)).thenReturn(false);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.delete("/messages/" + messageId );
        MvcResult response = mvc.perform(request).andReturn();

        assert response.getResponse().getStatus() == HttpStatus.NOT_FOUND.value();
    }
}