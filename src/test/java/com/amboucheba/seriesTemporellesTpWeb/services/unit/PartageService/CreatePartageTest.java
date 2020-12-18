package com.amboucheba.seriesTemporellesTpWeb.services.unit.PartageService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.*;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PartageService.class)
public class CreatePartageTest {


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
//    public void stExists_userExists__CreatePartage(){
//        SerieTemporelle st = new SerieTemporelle(1L, "st", "desc", null);
//        User user = new User(1L, "user", "pass");
//
//        Mockito.when(userService.find(1L, null)).thenReturn(user);
//        Mockito.when(serieTemporelleService.find(1L)).thenReturn(st);
//
//        PartageRequest input = new PartageRequest(1L, 1L, "r");
//        Partage toSave = new Partage(user, st, "r");
//        Partage expected = new Partage(1L, user, st, "r");
//
//        Mockito.when(partageRepository.save(toSave)).thenReturn(expected);
//
//        Partage returned = partageService.createPartage(input);
//
//        assertEquals(expected, returned);
//    }
//
//    @Test
//    public void stDoesNotExist__ThrowNotFoundException(){
//
//        Mockito.when(serieTemporelleService.find(1L)).thenThrow(NotFoundException.class);
//
//        assertThrows(NotFoundException.class, () -> {
//            PartageRequest pr = new PartageRequest();
//            pr.setSerieTemporelleId(1L);
//            partageService.createPartage(pr);
//        });
//    }
//
//    @Test
//    public void userDoesNotExist__ThrowNotFoundException(){
//
//        Mockito.when(userService.find(1L, null)).thenThrow(NotFoundException.class);
//
//        assertThrows(NotFoundException.class, () -> {
//            PartageRequest pr = new PartageRequest();
//            pr.setUserId(1L);
//            partageService.createPartage(pr);
//        });
//    }

}
