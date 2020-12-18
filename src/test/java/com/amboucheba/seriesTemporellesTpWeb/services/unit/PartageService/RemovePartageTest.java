package com.amboucheba.seriesTemporellesTpWeb.services.unit.PartageService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.EventService;
import com.amboucheba.seriesTemporellesTpWeb.services.PartageService;
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
@WebMvcTest(PartageService.class)
public class RemovePartageTest {

    @MockBean
    private PartageRepository partageRepository;

    @MockBean
    SerieTemporelleService serieTemporelleService;

    @MockBean
    UserService userService;

    @Autowired
    private PartageService partageService;

    @TestConfiguration
    static class Config{

        @Bean
        public PartageService getService(){
            return new PartageService();
        }
    }


//    @Test
//    public void partageExists__RemovePartage() {
//
//        Mockito.when(partageRepository.existsById(1L)).thenReturn(true);
//        Mockito.doNothing().when(partageRepository).deleteById(1L);
//
//        partageService.removePartage(1L);
//
//        Mockito.verify(partageRepository, Mockito.times(1)).deleteById(1L);
//    }
//
//    @Test
//    public void partageDoesNotExist__ThrowNotFoundException(){
//
//        Mockito.when(partageRepository.existsById(1L)).thenReturn(false);
//
//        assertThrows(NotFoundException.class, () -> {
//            partageService.removePartage(1L);
//        });
//    }
}
