package com.amboucheba.etudeDeCasWeb.Services.Unit.UserService;

import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.RegisterUserInput;
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
public class RegisterUsersTest {

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
    public void usernameDoesNotExist__createUser(){
        Users toSave = new Users("user", "pass");
        Users expected = new Users( 1L,"user", "pass");

        Mockito.when(usersRepository.findByUsername("user")).thenReturn(Optional.empty());

        Mockito.when(usersRepository.save(toSave)).thenReturn(expected);

        RegisterUserInput r = new RegisterUserInput("user", "pass");
        Users returned = usersService.registerUser(r);

        assertEquals(expected.getUsername(), returned.getUsername());
    }

    @Test
    public void usernameAlreadyExists__throwDuplicateResourceException(){
        Users toSave = new Users("user", "pass");
        RegisterUserInput r = new RegisterUserInput(toSave.getUsername(), "pass");
        Mockito.when(usersRepository.findByUsername("user")).thenReturn(Optional.of(toSave));

        assertThrows(DuplicateResourceException.class, () -> {
           usersService.registerUser(r);
        });
    }
}
