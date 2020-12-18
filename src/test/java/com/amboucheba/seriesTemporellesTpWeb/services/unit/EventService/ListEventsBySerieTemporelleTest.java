package com.amboucheba.seriesTemporellesTpWeb.services.unit.EventService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.EventService;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventService.class)
public class ListEventsBySerieTemporelleTest {


    @MockBean
    private EventRepository eventRepository;

    @MockBean
    SerieTemporelleService serieTemporelleService;

    @Autowired
    private EventService eventService;

    @TestConfiguration
    static class Config{

        @Bean
        public EventService getService(){
            return new EventService();
        }
    }

//    @Test
//    public void stExists__returnEventsOfSt() {
//        SerieTemporelle st = new SerieTemporelle(1L, "title", "desc", null);
//        List<Event> toBeReturned = Collections.singletonList(
//                new Event(1L, new Date(), 5.0f,"comment", st)
//        );
//        Mockito.when(serieTemporelleService.find(1L)).thenReturn(st);
//        Mockito.when(eventRepository.findBySerieTemporelleId(1L)).thenReturn(toBeReturned);
//
//        List<Event> events = eventService.listEventsBySerieTemporelle(1L);
//
//        assertEquals(toBeReturned, events);
//
//    }

//    @Test
//    public void stDoesNotExist__ThrowNotFoundException(){
//
//        Mockito.when(serieTemporelleService.find(1L)).thenThrow(NotFoundException.class);
//
//        assertThrows(NotFoundException.class, () -> {
//            eventService.listEventsBySerieTemporelle(1L);
//        });
//    }
}
