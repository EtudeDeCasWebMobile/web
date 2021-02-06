package com.amboucheba.etudeDeCasWeb.Services.Unit.PartageService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Partage;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.PartageRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.PartageRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Services.*;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@WebMvcTest(PartageService.class)
public class UpdatePartageTest {

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
    public void partageExists__returnUpdatedPartage() {
        Users users = new Users(1L, "user", "pass");
        Users shareWith = new Users(2L, "user2", "pass");
        SerieTemporelle st = new SerieTemporelle(1L, "st", "desc", users);
        Partage partage = new Partage(1L, shareWith, st, "r");
        Mockito.when(partageRepository.findById(1L)).thenReturn(Optional.of(partage));

        Partage newPartage = new Partage(1L, shareWith, st, "w");
        Mockito.when(partageRepository.save(newPartage)).thenReturn(newPartage);

        // Suppose user is authenticated
        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);

        PartageRequest pr = new PartageRequest( 1L, 1L, "w");
        Partage updatedPartage = partageService.updatePartage(pr, 1L, 1L);

        assertEquals(newPartage, updatedPartage);
    }

    @Test
    public void partageDoesNotExist__ThrowNotFoundException(){

        Mockito.when(partageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            partageService.updatePartage( new PartageRequest(), 1L, 1L);
        });
    }

}
