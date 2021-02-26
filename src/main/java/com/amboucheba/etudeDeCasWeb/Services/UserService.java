package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.DuplicateResourceException;
import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.RegisterInput;
import com.amboucheba.etudeDeCasWeb.Repositories.UserRepository;
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

    public User find(long userId){
        Optional<User> result = userRepository.findById(userId);
        // check that targeted user exists
        if (result.isEmpty()){
            throw new NotFoundException("'User' with id " + userId + " not found");
        }
        return result.get();
    }

    public List<User> listUsers(){
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .peek(user -> {
                    user.setPassword(null);
                    user.setPosition(null);
                })
                .collect(Collectors.toList());
    }

    public User registerUser(RegisterInput user){

        Optional<User> byEmail = userRepository.findByEmail(user.getEmail());

        if (byEmail.isPresent()){
            throw new DuplicateResourceException("'User' with username " + user.getEmail() + " already exists");
        }

        return userRepository.save(new User(user.getEmail(), passwordEncoder.encode(user.getPassword())));
    }
}
