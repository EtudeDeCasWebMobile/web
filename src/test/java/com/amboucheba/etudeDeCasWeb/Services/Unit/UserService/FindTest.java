package com.amboucheba.etudeDeCasWeb.Services.Unit.UserService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UsersService.class)
public class FindTest {

    @MockBean
    UsersRepository usersRepository;

    @Autowired
    UsersService usersService;

    @TestConfiguration
    static class Config{

        @Bean
        public JwtUtil getUtil(){
            return new JwtUtil();
        }

        @Bean
        public AuthService getpE(){
            return new AuthService();
        }

        @Bean
        public UsersService getSTService(){
            return new UsersService();
        }
    }

    @Test
    public void userExists__returnUser(){

        long userId = 1;
        Users users = new Users("user", "pass");
        Mockito.when(usersRepository.findById(userId)).thenReturn(Optional.of(users));

        Users found = usersService.find(userId, userId);

        assertEquals(found, users);
    }

    @Test
    public void serieTemporelleNotFound() {

        Mockito.when(usersRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            usersService.find(1L, 1L);
        });
    }
}
