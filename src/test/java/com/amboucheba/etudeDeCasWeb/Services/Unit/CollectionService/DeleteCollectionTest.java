package com.amboucheba.etudeDeCasWeb.Services.Unit.CollectionService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.CollectionInput;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CollectionService.class)
public class DeleteCollectionTest {

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
    public void collectionDoesNotExist__throwNotFoundException(){

        long collectionId = 1;

        Mockito.when(collectionRepository.findById(collectionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            collectionService.deleteCollection( collectionId, null);
        });
    }

    @Test
    public void collectionExist_userIdCorrect__throwNotFoundException(){

        long collectionId = 1;
        long userId = 1;
        User owner = new User(userId, "email", "password");
        Collection collection = new Collection("tag", owner);
        collection.setId(collectionId);

        Mockito.when(collectionRepository.findById(collectionId)).thenReturn(Optional.of(collection));

        assertDoesNotThrow(() -> {
            collectionService.deleteCollection( collectionId, userId);
        });
    }

}
