package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.DuplicateResourceException;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<User> listUsers(){
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public User find(long userId){
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()){
            throw new NotFoundException("'User' with id " + userId + " not found");
        }
        return user.get();
    }

    public User registerUser(User user){

        Optional<User> _user = userRepository.findByUsername(user.getUsername());

        if (_user.isPresent()){
            throw new NotFoundException("'User' with username " + user.getUsername() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }

}
