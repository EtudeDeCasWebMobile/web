package com.amboucheba.etudeDeCasWeb.Services.Unit.CollectionService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Repositories.CollectionRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.CollectionService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CollectionService.class)
public class ListCollectionsByUserTest {

    @MockBean
    CollectionRepository collectionRepository;

    @MockBean
    UserService userService;

    @Autowired
    CollectionService collectionService;

    @TestConfiguration
    static class Config{

        @MockBean
        public UserRepository userRepository;

        @Bean
        public JwtUtil getUtil(){
            return new JwtUtil();
        }

        @Bean
        public AuthService getpE(){
            return new AuthService();
        }

        @Bean
        public CollectionService getService(){
            return new CollectionService();
        }

    }

    @Test
    public void userDoesNotExist__throwNotFoundException(){

        long userId = 1;

        Mockito.when(userService.find(userId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            collectionService.listCollectionsByUser( userId);
        });
    }

    @Test
    public void userExists__returnListOfCollectionsOfUser(){

        long userId = 1;
        User owner = new User(userId, "username", "password");

        Mockito.when(userService.find(userId)).thenReturn(owner);
        Mockito.when(collectionRepository.findByOwnerId(userId)).thenReturn(Arrays.asList(
                new Collection( "c1", owner),
                new Collection("c2", owner)
        ));

        List<Collection> collections = collectionService.listCollectionsByUser( userId);

        assertEquals(2, collections.size());
        assertEquals("c1", collections.get(0).getTag());
        assertEquals(owner, collections.get(0).getOwner());
        assertEquals("c2", collections.get(1).getTag());
        assertEquals(owner, collections.get(1).getOwner());

    }
}
