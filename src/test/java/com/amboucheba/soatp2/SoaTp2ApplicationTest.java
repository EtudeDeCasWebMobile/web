package com.amboucheba.soatp2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SoaTp2Application.class)
class SoaTp2ApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void home() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders.get("/");
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();
        String expected = "<html><body><p> Home page </p></body></html>";

        assertEquals(expected, response_str);

    }

    @Test
    void param() throws Exception {

        String param = "Salut!";
        RequestBuilder request = MockMvcRequestBuilders.get("/" + param);
        MvcResult response = mvc.perform(request).andReturn();
        String response_str = response.getResponse().getContentAsString();

        String expected = "<html><body><p> You entered : " + param + " </p></body></html>";

        assertEquals(expected, response_str);
    }
}