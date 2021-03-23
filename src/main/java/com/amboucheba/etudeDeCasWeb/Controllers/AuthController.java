package com.amboucheba.etudeDeCasWeb.Controllers;

import com.amboucheba.etudeDeCasWeb.Models.Inputs.AuthInput;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Services.AuthService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @PostMapping(
            value = "/auth",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Auth token returned in AuthToken response header"),
            @ApiResponse(code = 400, message = "Bad Credentials, check email and password ")
    })
    ResponseEntity<User> authenticate (@Valid @RequestBody AuthInput auth){
        System.out.println("Entered");

        Optional<User> user = userRepository.findByEmail(auth.getEmail());
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User with email = " + auth.getEmail() + " not found.");
        }

        User _user = user.get();
        _user.setPassword("");
//        _user.setUsername("");

        String token = authService.authenticate(auth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("AuthToken", token);
        System.out.println("token = " + token);
        return new ResponseEntity<>(_user, headers, HttpStatus.OK);
    }
}
