package com.amboucheba.seriesTemporellesTpWeb.services.unit.UserService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserService.class)
public class FindTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @TestConfiguration
    static class Config{

        @Bean
        public UserService getSTService(){
            return new UserService();
        }
    }

    @Test
    public void userExists__returnUser(){

        long userId = 1;
        User user = new User("user", "pass");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User found = userService.find(userId);

        assertEquals(found, user);
    }

    @Test
    public void serieTemporelleNotFound() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.find(1L);
        });
    }
}
