package com.amboucheba.etudeDeCasWeb.Controllers.ToDelete;

import com.amboucheba.etudeDeCasWeb.Models.AuthenticationRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.UsersRepository;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.AuthService;
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
    UsersRepository usersRepository;


    @PostMapping("/authenticate")
    ResponseEntity<Users> authenticate (@RequestBody AuthenticationRequest auth){

        String token = authService.authenticate(auth);
        Optional<Users> user = usersRepository.findByUsername(auth.getUsername());
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User with username = " + auth.getUsername() + " not found.");
        }
        Users _users = user.get();
        _users.setPassword("");
        _users.setUsername("");

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        return new ResponseEntity<Users>(_users, headers, HttpStatus.OK);
    }

}
