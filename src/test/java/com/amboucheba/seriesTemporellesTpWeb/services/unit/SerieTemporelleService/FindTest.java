package com.amboucheba.seriesTemporellesTpWeb.services.unit.SerieTemporelleService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
import com.amboucheba.seriesTemporellesTpWeb.services.UserService;
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

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SerieTemporelleService.class)
public class FindTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    public void serieTemporelleExists() throws Exception {

        long stId = 1;
        SerieTemporelle st = new SerieTemporelle("title", "description");
        Mockito.when(stRepository.findById(stId)).thenReturn(Optional.of(st));

        SerieTemporelle found = serieTemporelleService.find(stId);

        assertEquals(found, st);
    }

    @Test
    public void serieTemporelleNotFound() {

        Mockito.when(stRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            serieTemporelleService.find(1L);
        });
    }

}
