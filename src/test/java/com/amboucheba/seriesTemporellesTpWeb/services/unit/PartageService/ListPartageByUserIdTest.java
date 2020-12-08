package com.amboucheba.seriesTemporellesTpWeb.services.unit.PartageService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PartageService.class)
public class ListPartageByUserIdTest {


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

    @Test
    public void userExists__returnPartagesOfUser() {
        User user = new User(1L, "user", "pass");
        List<Partage> toBeReturned = Collections.singletonList(
                new Partage(1L,  user,null, "r")
        );
        Mockito.when(userService.find(1L)).thenReturn(user);
        Mockito.when(partageRepository.findByUserId(1L)).thenReturn(toBeReturned);

        List<Partage> partages = partageService.listPartageByUserId(1L);

        assertEquals(toBeReturned, partages);
    }

    @Test
    public void userDoesNotExist__ThrowNotFoundException(){

        Mockito.when(userService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            partageService.listPartageByUserId(1L);
        });
    }
}
