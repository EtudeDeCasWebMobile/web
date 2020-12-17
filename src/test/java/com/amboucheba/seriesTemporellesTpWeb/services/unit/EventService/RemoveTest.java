package com.amboucheba.seriesTemporellesTpWeb.services.unit.EventService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.EventService;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
import com.amboucheba.seriesTemporellesTpWeb.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventService.class)
public class RemoveTest {

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
//    public void eventExists__returnSTsOfUser() {
//
//        Mockito.when(eventRepository.existsById(1L)).thenReturn(true);
//        Mockito.doNothing().when(eventRepository).deleteById(1L);
//
//        eventService.remove(1L);
//
//        Mockito.verify(eventRepository, Mockito.times(1)).deleteById(1L);
//    }
//
//    @Test
//    public void eventDoesNotExist__ThrowNotFoundException(){
//
//        Mockito.when(eventRepository.existsById(1L)).thenReturn(false);
//
//        assertThrows(NotFoundException.class, () -> {
//            eventService.remove(1L);
//        });
//    }
}
