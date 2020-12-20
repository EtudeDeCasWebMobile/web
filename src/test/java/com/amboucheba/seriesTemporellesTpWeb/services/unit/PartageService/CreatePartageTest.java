package com.amboucheba.seriesTemporellesTpWeb.services.unit.PartageService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.*;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.AuthService;
import com.amboucheba.seriesTemporellesTpWeb.services.PartageService;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
import com.amboucheba.seriesTemporellesTpWeb.services.UserService;
import com.amboucheba.seriesTemporellesTpWeb.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

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
        public PartageService getService(){
            return new PartageService();
        }
    }

    @Test
    public void stExists_userExists__CreatePartage(){
        User user = new User(1L, "user", "pass");
        User shareWith = new User(2L, "user2", "pass");
        SerieTemporelle st = new SerieTemporelle(1L, "st", "desc", user);

        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(userService.initiatorIsOwner(2L, 1L)).thenReturn(false);
        Mockito.when(userService.find(1L)).thenReturn(user);
        Mockito.when(userService.find(2L)).thenReturn(shareWith);
        Mockito.when(serieTemporelleService.find(1L)).thenReturn(st);

        PartageRequest input = new PartageRequest(2L, 1L, "r");
        Partage toSave = new Partage(shareWith, st, "r");
        Partage expected = new Partage(1L, shareWith, st, "r");

        Mockito.when(partageRepository.save(toSave)).thenReturn(expected);

        Partage returned = partageService.createPartage(input, 1L);

        assertEquals(expected, returned);
    }

    @Test
    public void stDoesNotExist__ThrowNotFoundException(){
        User user = new User(1L, "user", "pass");
        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
        Mockito.when(userService.find(1L)).thenReturn(user);
        Mockito.when(serieTemporelleService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            PartageRequest pr = new PartageRequest();
            pr.setSerieTemporelleId(1L);
            pr.setUserId(1L);
            partageService.createPartage(pr, 1L);
        });
    }

    @Test
    public void userDoesNotExist__ThrowNotFoundException(){

        // Suppose user is authenticated
        Mockito.when(userService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            PartageRequest pr = new PartageRequest();
            pr.setUserId(1L);
            partageService.createPartage(pr, 1L);
        });
    }

}
