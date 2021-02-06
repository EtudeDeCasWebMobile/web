package com.amboucheba.etudeDeCasWeb.Services.Unit.PartageService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Partage;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.PartageRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.PartageRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.PartageService;
import com.amboucheba.etudeDeCasWeb.Services.SerieTemporelleService;
import com.amboucheba.etudeDeCasWeb.Services.UsersService;
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
    public void stExists_userExists__CreatePartage(){
        Users users = new Users(1L, "user", "pass");
        Users shareWith = new Users(2L, "user2", "pass");
        SerieTemporelle st = new SerieTemporelle(1L, "st", "desc", users);

        // Suppose user is authenticated
        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(usersService.initiatorIsOwner(2L, 1L)).thenReturn(false);
        Mockito.when(usersService.find(1L)).thenReturn(users);
        Mockito.when(usersService.find(2L)).thenReturn(shareWith);
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
        Users users = new Users(1L, "user", "pass");
        // Suppose user is authenticated
        Mockito.when(usersService.initiatorIsOwner(Mockito.anyLong(), Mockito.anyLong())).thenReturn(false);
        Mockito.when(usersService.find(1L)).thenReturn(users);
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
        Mockito.when(usersService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            PartageRequest pr = new PartageRequest();
            pr.setUserId(1L);
            partageService.createPartage(pr, 1L);
        });
    }

}
