package com.amboucheba.etudeDeCasWeb.Services.Unit.UserService;

import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.RegisterInput;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.UsersService;
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
        public JwtUtil getUtil(){
            return new JwtUtil();
        }

        @Bean
        public AuthService getpE(){
            return new AuthService();
        }

        @Bean
        public UserService getSTService(){
            return new UserService();
        }
    }
    @Test
    public void usernameAndEmailDoNotExist__createUser(){
        User toSave = new User("email", "pass");
        User expected = new User( 1L,"email",  "pass");

        Mockito.when(userRepository.findByEmail("user")).thenReturn(Optional.empty());

        Mockito.when(userRepository.save(toSave)).thenReturn(expected);

        RegisterInput r = new RegisterInput("email", "pass");
        User returned = userService.registerUser(r);

        assertEquals(expected.getEmail(), returned.getEmail());
    }

    @Test
    public void usernameAlreadyExists__throwDuplicateResourceException(){
        User toSave = new User("email", "pass");
        RegisterInput r = new RegisterInput(toSave.getEmail(),  "pass");
        Mockito.when(userRepository.findByEmail(r.getEmail())).thenReturn(Optional.of(toSave));

        assertThrows(DuplicateResourceException.class, () -> {
           userService.registerUser(r);
        });
    }

    @Test
    public void emailAlreadyExists__throwDuplicateResourceException(){
        User toSave = new User("email", "pass");
        RegisterInput r = new RegisterInput(toSave.getEmail(), "pass");
        Mockito.when(userRepository.findByEmail(r.getEmail())).thenReturn(Optional.of(toSave));

        assertThrows(DuplicateResourceException.class, () -> {
            userService.registerUser(r);
        });
    }
}
