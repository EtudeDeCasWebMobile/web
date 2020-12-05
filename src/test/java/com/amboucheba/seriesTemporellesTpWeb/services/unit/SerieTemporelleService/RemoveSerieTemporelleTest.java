package com.amboucheba.seriesTemporellesTpWeb.services.unit.SerieTemporelleService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
import com.amboucheba.seriesTemporellesTpWeb.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SerieTemporelleService.class)
public class RemoveSerieTemporelleTest {

    @MockBean
    private SerieTemporelleRepository stRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private SerieTemporelleService serieTemporelleService;

    @TestConfiguration
    static class Config{

        @Bean
        public SerieTemporelleService getSTService(){
            return new SerieTemporelleService();
        }
    }

    @Test
    public void userExists__returnSTsOfUser() {

        Mockito.when(stRepository.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(stRepository).deleteById(1L);

        serieTemporelleService.removeSerieTemporelle(1L);

        Mockito.verify(stRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void stDoesNotExist__ThrowNotFoundException(){

        Mockito.when(stRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            serieTemporelleService.removeSerieTemporelle(1L);
        });
    }
}
