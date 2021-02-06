package com.amboucheba.etudeDeCasWeb.Services.Unit.EventService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.EventRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventService.class)
public class AddEventToSerieTemporelleTest {


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
    public void stExists__addEventToSt(){
        Users users = new Users(1L, "user", "pass");
        SerieTemporelle st = new SerieTemporelle(1L,"event", "pass", users);
        Event event = new Event(new Date(), 5.0f,"comment");
        Event toSave = new Event( new Date(), 5.0f,"comment", st);
        Event saved = new Event( 1L,new Date(), 5.0f,"comment", st);

        // Suppose user is authenticated
        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(serieTemporelleService.find(1L)).thenReturn(st);
        Mockito.when(eventRepository.save(toSave)).thenReturn(saved);

        Event returned = eventService.addEventToSerieTemporelle(1L, event, 1L);

        assertEquals(saved, returned);
    }

    @Test
    public void stDoesNotExist__ThrowNotFoundException(){

        Mockito.when(serieTemporelleService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            eventService.addEventToSerieTemporelle(1L, new Event(), 1L );
        });
    }

}
