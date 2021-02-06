package com.amboucheba.etudeDeCasWeb.Services.Unit.SerieTemporelleService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.SerieTemporelleRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.AuthService;
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

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(SerieTemporelleService.class)
public class CreateSerieTemporelleTest {

    @MockBean
    private SerieTemporelleRepository stRepository;

    @MockBean
    PartageService partageService;

    @MockBean
    private UsersService usersService;

    @Autowired
    private SerieTemporelleService serieTemporelleService;

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
        public SerieTemporelleService getSTService(){
            return new SerieTemporelleService();
        }
    }

    @Test
    public void userExists__createST(){
        Users users = new Users(1L, "user", "pass");
        SerieTemporelle st = new SerieTemporelle("title", "desc");
        SerieTemporelle toSave = new SerieTemporelle( "title", "desc", users);
        SerieTemporelle saved = new SerieTemporelle( 1L,"title", "desc", users);

        // Suppose user is authenticated
        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(usersService.find(1L)).thenReturn(users);
        Mockito.when(stRepository.save(toSave)).thenReturn(saved);

        SerieTemporelle returned = serieTemporelleService.createSerieTemporelle(st, 1L, 1L);

        assertEquals(saved, returned);
    }

    @Test
    public void userDoesNotExist__ThrowNotFoundException(){

        // Suppose user is authenticated
        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(usersService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            serieTemporelleService.createSerieTemporelle(new SerieTemporelle(), 1L, 1L);
        });
    }

}
