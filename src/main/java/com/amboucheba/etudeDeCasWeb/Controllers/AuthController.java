package com.amboucheba.etudeDeCasWeb.Controllers;

import com.amboucheba.etudeDeCasWeb.Models.Inputs.AuthInput;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/auth")
    ResponseEntity<User> authenticate (@RequestBody AuthInput auth){

        String token = authService.authenticate(auth);

        Optional<User> user = userRepository.findByUsername(auth.getUsername());
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User with username = " + auth.getUsername() + " not found.");
        }

        User _user = user.get();
        _user.setPassword("");
//        _user.setUsername("");

        HttpHeaders headers = new HttpHeaders();
        headers.set("AuthToken", token);
        return new ResponseEntity<User>(_user, headers, HttpStatus.OK);
    }
}