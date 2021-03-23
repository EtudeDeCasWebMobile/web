package com.amboucheba.etudeDeCasWeb.Services.Unit.LocationService;

import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class ListLocationsByUserTest {


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

        long userId = 1;

        Mockito.when(userService.find(userId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> {
            locationService.listLocationsByUser(userId);
        });
    }



    @Test
    public void userExists__returnListOfLocationsOfUser(){

        long userId = 1;
        User owner = new User(userId, "username", "password");

        Mockito.when(userService.find(userId)).thenReturn(owner);
        Mockito.when(locationRepository.findByOwnerId(userId)).thenReturn(Arrays.asList(
                new Location( "c1","d1", owner),
                new Location("c2", "d2",owner)
        ));

        List<Location> locations = locationService.listLocationsByUser( userId);

        assertEquals(2, locations.size());
        assertEquals("c1", locations.get(0).getTitle());
        assertEquals("d1", locations.get(0).getDescription());
        assertEquals(owner, locations.get(0).getOwner());

        assertEquals("c2", locations.get(1).getTitle());
        assertEquals("d2", locations.get(1).getDescription());
        assertEquals(owner, locations.get(1).getOwner());

    }
}