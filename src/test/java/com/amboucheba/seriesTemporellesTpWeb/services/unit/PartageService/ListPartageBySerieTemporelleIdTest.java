package com.amboucheba.seriesTemporellesTpWeb.services.unit.PartageService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PartageService.class)
public class ListPartageBySerieTemporelleIdTest {


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
    public void stExists__returnPartagesOfSt() {
        User user = new User(1L, "user", "pass");
        User shareWith = new User(2L, "user2", "pass");
        SerieTemporelle st = new SerieTemporelle(1L, "st", "desc", user);
        List<Partage> toBeReturned = Collections.singletonList(
                new Partage(1L,  shareWith, st, "r")
        );
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(serieTemporelleService.find(1L)).thenReturn(st);
        Mockito.when(partageRepository.findBySerieTemporelleId(1L)).thenReturn(toBeReturned);

        List<Partage> partages = partageService.listPartageBySerieTemporelleId(1L, 1L);

        assertEquals(toBeReturned, partages);
    }

    @Test
    public void stDoesNotExist__ThrowNotFoundException(){

        Mockito.when(serieTemporelleService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            partageService.listPartageBySerieTemporelleId(1L, 1L);
        });
    }
}
