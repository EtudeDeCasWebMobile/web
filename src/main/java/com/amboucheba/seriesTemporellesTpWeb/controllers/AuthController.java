package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.models.AuthDetails;
import com.amboucheba.seriesTemporellesTpWeb.models.AuthenticationRequest;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;


    @PostMapping("/authenticate")
    ResponseEntity<User> authenticate (@RequestBody AuthenticationRequest auth){

        String token = authService.authenticate(auth);
        Optional<User> user = userRepository.findByUsername(auth.getUsername());
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User with username = " + auth.getUsername() + " not found.");
        }
        User _user = user.get();
        _user.setPassword("");
        _user.setUsername("");

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        return new ResponseEntity(_user, headers, HttpStatus.FOUND);
    }

}
