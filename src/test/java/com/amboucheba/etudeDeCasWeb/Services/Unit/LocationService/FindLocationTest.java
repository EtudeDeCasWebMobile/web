package com.amboucheba.etudeDeCasWeb.Services.Unit.LocationService;
import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.LocationInput;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.RegisterInput;
import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LocationService.class)
public class FindLocationTest {

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
    public void locationDoesNotExist__throwNotFoundException(){

        long locationId = 1;

        Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            locationService.find( locationId, 1);
        });
    }

    @Test
    public void locationExists_userIsTheOwner__throwNotFoundException(){

        long userId = 1;
        User owner = new User(userId, "username", "password");
        Location location= new Location("title","description", owner);
        long locationId = 1;

        Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        Location returned = locationService.find( locationId, 1);

        assertEquals(location.getTitle(), returned.getTitle());
        assertEquals(location.getDescription(), returned.getDescription());
        assertEquals(location.getOwner(), returned.getOwner());
    }
}

