package com.amboucheba.etudeDeCasWeb.Services.Unit.ToDelete.EventService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.EventRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.*;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
    UsersService usersService;

    @MockBean
    PartageService partageService;

    @TestConfiguration
    static class Config{

        @MockBean
        public UsersRepository usersRepository;

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

        Users users = new Users(1L, "user", "pass");
        SerieTemporelle st = new SerieTemporelle(1L,"event", "pass", users);

        long eventId = 1;
        Event event = new Event(1L, new Date(), 5.0f,"comment", st);
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        // Suppose user is authenticated
        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);

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