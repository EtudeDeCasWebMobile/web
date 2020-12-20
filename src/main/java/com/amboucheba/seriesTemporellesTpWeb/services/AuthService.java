package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.models.AuthDetails;
import com.amboucheba.seriesTemporellesTpWeb.models.AuthenticationRequest;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import com.amboucheba.seriesTemporellesTpWeb.util.JwtUtil;
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
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("User with username = " + username + " not found.");
        }

        User _user = user.get();

        return new AuthDetails(_user.getId(), _user.getUsername(), _user.getPassword());
    }

    public String authenticate(AuthenticationRequest auth){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword())
        );

        AuthDetails userDetails = loadUserByUsername(auth.getUsername());
        return jwtUtil.generateToken(userDetails);
    }
}
