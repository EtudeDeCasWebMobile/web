package com.amboucheba.seriesTemporellesTpWeb.resources.unit.MessageResource;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.ApiException;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
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

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageResource.class)
class UpdateMessageTest {

    @MockBean
    MessageRepository messageRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // Happy path 1 : message already exists -> update it
    void updateExistingMessage() throws Exception {
        Long messageId = 1L;
        Message message = new Message(messageId, "User 1", "Text", null);
        Message updatedMessage = new Message(messageId, "User 1", "Updated Text", null);
        Mockito.when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        Mockito.when(messageRepository.save(message)).thenReturn(updatedMessage);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/messages/" + messageId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(message));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expectedResponse = objectMapper.writeValueAsString(updatedMessage);

        assertEquals(expectedResponse, response_str);
        assertEquals(HttpStatus.OK.value(), response.getResponse().getStatus());
    }

    @Test
    // Happy path 2 : message does not exist -> create it
    void messageDoesNotExist() throws Exception {
        Long messageId = 1L;
        Message message = new Message(messageId, "User 1", "Text", null);

        Mockito.when(messageRepository.findById(messageId)).thenReturn(Optional.empty());
        Mockito.when(messageRepository.save(message)).thenReturn(message);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/messages/" + messageId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(message));
        MvcResult response = mvc.perform(request).andReturn();

        assertEquals(HttpStatus.CREATED.value(), response.getResponse().getStatus());

        String location = response.getResponse().getHeader("location");

        assertTrue(location.endsWith("/messages/" + messageId));
    }

    @Test
        // Same thing for text
    void usernameMissing() throws Exception {

        Long messageId = 1L;
        // Not setting username
        Message message = new Message();
        message.setId(messageId);
        message.setText("This is the new text");

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/messages/" + messageId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(message));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }

    @Test
    void usernameTooShort() throws Exception {

        Long messageId = 1L;
        // username must be between 6 and 255
        Message message = new Message( messageId, "User", "Message 1", new Date());

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/messages/" + messageId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(message));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }

    @Test
        // Same thing for username
    void textTooLarge() throws Exception {

        Long messageId = 1L;
        String text = "a".repeat(256);
        Message message = new Message( messageId, "User 1", text, new Date());

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.put("/messages/" + messageId )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(message));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }
}