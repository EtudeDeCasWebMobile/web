package com.amboucheba.etudeDeCasWeb.Services.Unit.ToDelete.PartageService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Partage;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.PartageRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.PartageService;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.SerieTemporelleService;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.UsersService;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
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
    UsersService usersService;

    @Autowired
    private PartageService partageService;

    @TestConfiguration
    static class Config{

        @MockBean
        public UsersRepository usersRepository;

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
        Users users = new Users(1L, "user", "pass");
        Users shareWith = new Users(2L, "user2", "pass");
        SerieTemporelle st = new SerieTemporelle(1L, "st", "desc", users);
        List<Partage> toBeReturned = Collections.singletonList(
                new Partage(1L,  shareWith, st, "r")
        );
        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);
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
