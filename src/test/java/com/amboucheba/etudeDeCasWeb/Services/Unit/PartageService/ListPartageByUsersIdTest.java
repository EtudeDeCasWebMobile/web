package com.amboucheba.etudeDeCasWeb.Services.Unit.PartageService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Partage;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.PartageRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.PartageService;
import com.amboucheba.etudeDeCasWeb.Services.SerieTemporelleService;
import com.amboucheba.etudeDeCasWeb.Services.UserService;
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
public class ListPartageByUsersIdTest {


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
    public void userExists__returnPartagesOfUser() {
        Users users = new Users(1L, "user", "pass");
        Users shareWith = new Users(2L, "user2", "pass");
        SerieTemporelle st = new SerieTemporelle(1L, "st", "desc", users);
        List<Partage> toBeReturned = Collections.singletonList(
                new Partage(1L,  shareWith, st, "r")
        );
        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(userService.find(1L)).thenReturn(users);
        Mockito.when(partageRepository.findByUsersId(1L)).thenReturn(toBeReturned);

        List<Partage> partages = partageService.listPartageByUserId(1L, 1L);

        assertEquals(toBeReturned, partages);
    }

    @Test
    public void userDoesNotExist__ThrowNotFoundException(){

        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(userService.find(1L)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            partageService.listPartageByUserId(1L, 1L);
        });
    }
}
