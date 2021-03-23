package com.amboucheba.etudeDeCasWeb.Services.Unit.LocationService;

import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.LocationService;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



@ExtendWith(SpringExtension.class)
@WebMvcTest(LocationService.class)
public class CreateLocationTest {

    @MockBean
    LocationRepository locationRepository;

    @MockBean
    UserService userService;

    @Autowired
    LocationService locationService;

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
        public LocationService getService(){
            return new LocationService();
        }

    }
    @Test
    public void userDoesNotExist__throwNotFoundException(){

        String title = "title";
        long userId = 1;

        Mockito.when(userService.find(userId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            locationService.createLocation(title,"desription", userId);
        });
    }

    @Test
    public void tiitleAlreadyExists__throwDuplicateResourceException(){

        String title = "title";
        long userId = 1;

        Mockito.when(userService.find(userId)).thenReturn(new User());
        Mockito.when(locationRepository.findByTitleAndOwnerId(title, userId)).thenReturn(Optional.of(new Location()));

        assertThrows(DuplicateResourceException.class, () -> {
            locationService.createLocation(title,"description", userId);
        });
    }

    @Test
    public void userExists_TitleDoesNotExist__CreateLocation_ReturnIt(){

        String title = "title";
        long userId = 1;
        User owner = new User(userId, "username", "password");

        Mockito.when(userService.find(userId)).thenReturn(owner);
        Mockito.when(locationRepository.findByTitleAndOwnerId(title, userId)).thenReturn(Optional.empty());
        Mockito.when(locationRepository.save(new Location(title,"description", owner))).thenReturn(new Location( title,"description", owner));

        Location returned = locationService.createLocation(title,"description", userId);

        assertEquals(title, returned.getTitle());

        assertEquals(userId, returned.getOwner().getId());

    }
}
