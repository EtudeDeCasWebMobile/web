package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.models.AuthDetails;
import com.amboucheba.seriesTemporellesTpWeb.models.AuthenticationRequest;
import com.amboucheba.seriesTemporellesTpWeb.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;


    @PostMapping(
        value = "/authenticate",
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    ResponseEntity<Void> authenticate (@RequestBody AuthenticationRequest auth){

        String token = authService.authenticate(auth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        return ResponseEntity.noContent().headers(headers).build();
    }

}
