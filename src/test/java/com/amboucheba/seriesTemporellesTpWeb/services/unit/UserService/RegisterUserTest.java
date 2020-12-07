package com.amboucheba.seriesTemporellesTpWeb.services.unit.UserService;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.DuplicateResourceException;
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
public class RegisterUserTest {

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
    public void usernameDoesAlreadyExist__returnUser(){
        User toSave = new User("user", "pass");
        User expected = new User( 1L,"user", "pass");

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        Mockito.when(userRepository.save(toSave)).thenReturn(expected);

        User returned = userService.registerUser(toSave);

        assertEquals(expected, returned);
    }

    @Test
    public void usernameAlreadyExists__throwDuplicateResourceException(){
        User toSave = new User("user", "pass");

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(toSave));

        assertThrows(DuplicateResourceException.class, () -> {
           userService.registerUser(toSave);
        });
    }
}
