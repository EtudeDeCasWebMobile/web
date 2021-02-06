package com.amboucheba.etudeDeCasWeb.Services.Unit.EventService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.EventRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.*;
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


@ExtendWith(SpringExtension.class)
@WebMvcTest(EventService.class)
public class UpdateEventTest {

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
    public void eventExists__returnUpdatedEvent() {
        Users users = new Users(1L, "user", "pass");
        SerieTemporelle st = new SerieTemporelle(1L,"event", "pass", users);
        Event toUpdate = new Event(1L, new Date(), 5.0f,"comment", st);
        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.of(toUpdate));

        Event newEvent = new Event(1L, new Date(), 6.0f,"new comment", st);
        Mockito.when(eventRepository.save(newEvent)).thenReturn(newEvent);

        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);

        Event updatedST = eventService.updateEvent(1L, newEvent, 1L);

        assertEquals(newEvent, updatedST);
    }

    @Test
    public void stIdDoesNotExist__ThrowNotFoundException(){

        Mockito.when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            eventService.updateEvent(1L, new Event(), 1L);
        });
    }

}
