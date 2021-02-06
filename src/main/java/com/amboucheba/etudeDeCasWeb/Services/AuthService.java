package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.AuthenticationRequest;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public AuthDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User with username = " + username + " not found.");
        }

        Users _users = user.get();

        return new AuthDetails(_users.getId(), _users.getUsername(), _users.getPassword());
    }

    public String authenticate(AuthenticationRequest auth){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword())
        );

        AuthDetails userDetails = loadUserByUsername(auth.getUsername());
        return jwtUtil.generateToken(userDetails);
    }
}
