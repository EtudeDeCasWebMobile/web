//package com.amboucheba.etudeDeCasWeb.Services.Unit.LocationService;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//
//import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
//import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
//import com.amboucheba.etudeDeCasWeb.Repositories.LocationRepository;
//import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
//import com.amboucheba.etudeDeCasWeb.Services.AuthService;
//import com.amboucheba.etudeDeCasWeb.Services.LocationService;
//import com.amboucheba.etudeDeCasWeb.Services.UserService;
//import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
//
//
//public class FindTest {
//	
//
//	    @MockBean
//	    LocationRepository locationRepository;
//
//	    @MockBean
//	    UserService userService;
//
//	    @Autowired
//	    LocationService locationService;
//
//	    @TestConfiguration
//	    static class Config{
//
//	        @MockBean
//	        public UserRepository userRepository;
//
//	        @Bean
//	        public JwtUtil getUtil(){
//	            return new JwtUtil();
//	        }
//
//	        @Bean
//	        public AuthService getpE(){
//	            return new AuthService();
//	        }
//
//	        @Bean
//	        public LocationService getService(){
//	            return new LocationService();
//	        }
//
//	    }
//
//	    @Test
//	    public void locationDoesNotExist__throwNotFoundException(){
//	    	long userId = 1;
//	        
//	        User user = locationService.find(userId);
//	        
//	        Mockito.when(locationRepository.findByOwner(user)).thenReturn(Optional.empty());
//
//	        assertThrows(NotFoundException.class, () -> {
//	            locationService.find( userId, "");
//	        });
//	    }
//
//
//	
//}
