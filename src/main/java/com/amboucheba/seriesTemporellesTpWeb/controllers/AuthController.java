package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.models.AuthenticationRequest;
import com.amboucheba.seriesTemporellesTpWeb.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;


    @PostMapping("/authenticate")
    ResponseEntity<Void> authenticate (@RequestBody AuthenticationRequest auth){

        String token = authService.authenticate(auth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);

        return ResponseEntity.noContent().headers(headers).build();
    }

}
