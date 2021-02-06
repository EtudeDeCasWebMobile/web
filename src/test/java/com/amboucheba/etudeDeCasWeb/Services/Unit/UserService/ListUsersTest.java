package com.amboucheba.etudeDeCasWeb.Services.Unit.UserService;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UsersService.class)
public class ListUsersTest {

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
    public void __returnUserList(){

        List<Users> expected = Collections.singletonList(
                new Users("user", "pass")
        );
        Mockito.when(usersRepository.findAll()).thenReturn(expected);

        List<Users> users = usersService.listUsers();

        assertEquals(expected, users);
    }

}
