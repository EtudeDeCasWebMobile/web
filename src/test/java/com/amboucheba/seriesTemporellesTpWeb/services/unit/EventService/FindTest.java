package com.amboucheba.seriesTemporellesTpWeb.services.unit.EventService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.*;
import com.amboucheba.seriesTemporellesTpWeb.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventService.class)
public class FindTest {

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    SerieTemporelleService serieTemporelleService;

    @Autowired
    private EventService eventService;

    @MockBean
    UserService userService;

    @MockBean
    PartageService partageService;

    @TestConfiguration
    static class Config{

        @MockBean
        public UserRepository userRepository;

        @Bean
        public JwtUtil getUtil(){
            return new JwtUtil();
        }

        @Bean
        public AuthService getAuth(){
            return new AuthService();
        }

        @Bean
        public EventService getService(){
            return new EventService();
        }
    }

    @Test
    public void stExists__returnEventsOfSt() throws Exception {

        User user = new User(1L, "user", "pass");
        SerieTemporelle st = new SerieTemporelle(1L,"event", "pass", user);

        long eventId = 1;
        Event event = new Event(1L, new Date(), 5.0f,"comment", st);
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);

        Event found = eventService.find(eventId, 1L);

        assertEquals(found, event);
    }

    @Test
    public void stNotFound__throwNotFoundException() {

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            eventService.find(1L, 1L);
        });
    }

}
