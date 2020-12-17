package com.amboucheba.seriesTemporellesTpWeb.services.unit.SerieTemporelleService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
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

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(SerieTemporelleService.class)
public class CreateSerieTemporelleTest {


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

//    @Test
//    public void userExists__createST(){
//        User user = new User(1L, "user", "pass");
//        SerieTemporelle st = new SerieTemporelle("title", "desc");
//        SerieTemporelle toSave = new SerieTemporelle( "title", "desc", user);
//        SerieTemporelle saved = new SerieTemporelle( 1L,"title", "desc", user);
//
//        Mockito.when(userService.find(1L, null)).thenReturn(user);
//        Mockito.when(stRepository.save(toSave)).thenReturn(saved);
//
//        SerieTemporelle returned = serieTemporelleService.createSerieTemporelle(st, 1L);
//
//        assertEquals(saved, returned);
//    }
//
//    @Test
//    public void userDoesNotExist__ThrowNotFoundException(){
//
//        Mockito.when(userService.find(1L, null)).thenThrow(NotFoundException.class);
//
//        assertThrows(NotFoundException.class, () -> {
//            serieTemporelleService.createSerieTemporelle(new SerieTemporelle(), 1L);
//        });
//    }

}
