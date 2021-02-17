package com.amboucheba.etudeDeCasWeb.Services.Unit.AuthService;

import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.UsersService;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthService.class)
public class AuthenticateTest {

//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    @Autowired
//    JwtUtil jwtUtil;
//
//    @TestConfiguration
//    static class Config{
//
//        @Bean
//        public JwtUtil getUtil(){
//            return new JwtUtil();
//        }
//
//        @Bean
//        public AuthService getpE(){
//            return new AuthService();
//        }
//
//        @Bean
//        public AuthenticationManager getAM(){
//            return new AuthenticationManager() {
//                @Override
//                public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                    return null;
//                }
//            };
//        }
//
//        @Bean
//        public UsersService getSTService(){
//            return new UsersService();
//        }
//    }
}
