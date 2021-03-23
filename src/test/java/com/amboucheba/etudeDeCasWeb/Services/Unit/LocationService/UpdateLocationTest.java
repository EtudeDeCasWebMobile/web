
package com.amboucheba.etudeDeCasWeb.Services.Unit.LocationService;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.RegisterInput;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.LocationInput;
import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.LocationService;
import com.amboucheba.etudeDeCasWeb.Services.UserService;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
import org.junit.jupiter.api.Assertions;
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
public class UpdateLocationTest {

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
    public void locationExist_userIdCorrect__throwNotFoundException(){

        long locationId = 1;
        long userId = 1;
        String newTitle = "new title";
        User owner = new User(userId, "email", "password");
        Location location= new Location("title","description", owner);
        location.setId(locationId);
        Location updatedLocation= new Location(newTitle,"description", owner);
        updatedLocation.setId(locationId);

        Mockito.when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));
        Mockito.when(locationRepository.save(updatedLocation)).thenReturn(updatedLocation);

        LocationInput newLocation = new LocationInput(newTitle,"description");

        Location returned = locationService.updateLocation(newLocation, locationId, userId);

        assertEquals(newTitle, returned.getTitle());
        assertEquals("description", returned.getDescription());
        assertEquals(locationId, returned.getId());
    }


}

