//package com.amboucheba.etudeDeCasWeb.Services.Unit.LocationService;
//import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
//import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
//import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
//import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
//import com.amboucheba.etudeDeCasWeb.Services.AuthService;
//import com.amboucheba.etudeDeCasWeb.Services.LocationService;
//import com.amboucheba.etudeDeCasWeb.Services.ToDelete.*;
//import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(EventService.class)
//public class UpdateLocationTest {
//
//    @MockBean
//    private LocationRepository locationRepository;
//
//
//    @Autowired
//    private LocationService locationService;
//
//    @MockBean
//    UsersService usersService;
//
//    @TestConfiguration
//    static class Config{
//
//        @MockBean
//        public UsersRepository usersRepository;
//
//        @Bean
//        public JwtUtil getUtil(){
//            return new JwtUtil();
//        }
//
//        @Bean
//        public AuthService getAuth(){
//            return new AuthService();
//        }
//
//        @Bean
//        public EventService getService(){
//            return new EventService();
//        }
//    }
//
//
//    @Test
//    public void locationExists__returnUpdatedLocation() {
//        User user = new User(1L, "user", "pass");
//        Location toUpdate = new Location(1L, "title", "description1",user,"latitude1", "longitude1");
//        Mockito.when(locationRepository.findById(1L)).thenReturn(Optional.of(toUpdate));
//
//        Location newLocation = new Location(1L, "title", "description2", user,"latitude2", "longitude2");
//        Mockito.when(locationRepository.save(newLocation)).thenReturn(newLocation);
//
//        // Suppose user is authenticated
//        Mockito.when(usersService.initiatorIsOwner(1L, 1L)).thenReturn(true);
//
//        Location updatedST = locationService.updateCurrentLocation(1L, newLocation);
//
//        assertEquals(newLocation, updatedST);
//    }
//
//    
//
//}
