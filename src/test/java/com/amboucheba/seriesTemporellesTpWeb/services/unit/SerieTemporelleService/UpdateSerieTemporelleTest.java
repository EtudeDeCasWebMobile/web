package com.amboucheba.seriesTemporellesTpWeb.services.unit.SerieTemporelleService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.AuthService;
import com.amboucheba.seriesTemporellesTpWeb.services.PartageService;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
import com.amboucheba.seriesTemporellesTpWeb.services.UserService;
import com.amboucheba.seriesTemporellesTpWeb.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(SerieTemporelleService.class)
public class UpdateSerieTemporelleTest {

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
    public void stExists__returnUpdatedST() {
        User user = new User(1L, "", "");
        SerieTemporelle toUpdate = new SerieTemporelle(1L, "title", "desc", user);
        Mockito.when(stRepository.findById(1L)).thenReturn(Optional.of(toUpdate));

        SerieTemporelle newSerieTemporelle = new SerieTemporelle(1L, "newTitle", "newDesc", user);
        Mockito.when(stRepository.save(newSerieTemporelle)).thenReturn(newSerieTemporelle);

        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);

        SerieTemporelle updatedST = serieTemporelleService.updateSerieTemporelle(newSerieTemporelle, 1L, 1L);

        assertEquals(newSerieTemporelle, updatedST);
    }

    @Test
    public void stIdDoesNotExist__ThrowNotFoundException(){

        // Suppose user is authenticated
        Mockito.when(userService.initiatorIsOwner(1L, 1L)).thenReturn(true);
        Mockito.when(stRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            serieTemporelleService.updateSerieTemporelle(new SerieTemporelle(),1L, 1L);
        });
    }

}
