package com.amboucheba.etudeDeCasWeb.Services.Unit.SerieTemporelleService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.SerieTemporelleRepository;
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

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SerieTemporelleService.class)
public class FindTest {

    @MockBean
    private SerieTemporelleRepository stRepository;

    @MockBean
    private UserService userService;

    @MockBean
    PartageService partageService;

    @Autowired
    private SerieTemporelleService serieTemporelleService;

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
        public SerieTemporelleService getSTService(){
            return new SerieTemporelleService();
        }
    }

    @Test
    public void serieTemporelleExists() throws Exception {

        long stId = 1;
        Users users = new Users(1L, "", "");
        SerieTemporelle st = new SerieTemporelle("title", "description", users);

        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(stRepository.findById(stId)).thenReturn(Optional.of(st));

        SerieTemporelle found = serieTemporelleService.find(stId, 1L);

        assertEquals(found, st);
    }

    @Test
    public void serieTemporelleNotFound() {

        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(stRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            serieTemporelleService.find(1L, 1L);
        });
    }

}
