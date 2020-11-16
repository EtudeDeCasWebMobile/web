package com.amboucheba.soatp2.resources.unit.MessageResource;

import com.amboucheba.soatp2.exceptions.ApiException;
import com.amboucheba.soatp2.models.Message;
import com.amboucheba.soatp2.repositories.MessageRepository;
import com.amboucheba.soatp2.resources.MessageResource;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageResource.class)
class AddMessageTest {

    @MockBean
    MessageRepository messageRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    // Happy scenario
    void addMessage() throws Exception {

        Message message = new Message(  "User 1", "Message 1");
        Long messageId = 1L;
        Message savedMessage = new Message(messageId, "User 1", "Message 1", new Date());
        Mockito.when(messageRepository.save(message)).thenReturn(savedMessage);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.post("/messages" )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(message));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.CREATED.value(), response.getResponse().getStatus());

        String location = response.getResponse().getHeader("location");

        assertTrue(location.endsWith("/messages/" + messageId));
    }

    @Test
    // Same thing for text
    void usernameMissing() throws Exception {
        // Not setting username
        Message message = new Message();
        message.setText("This is the new text");

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.post("/messages" )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(message));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }

    @Test
    void usernameTooShort() throws Exception {
        // username must be between 6 and 255
        Message message = new Message(  "User", "Message 1");

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.post("/messages" )
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

        String text = "a".repeat(256);
        Message message = new Message(  "User 1", text);

        // Send request to endpoint
        RequestBuilder request = MockMvcRequestBuilders.post("/messages" )
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(message));
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        ApiException responseException = objectMapper.readerFor(ApiException.class).readValue(response_str);

        assert responseException.getStatus().equals(HttpStatus.BAD_REQUEST);

    }
}