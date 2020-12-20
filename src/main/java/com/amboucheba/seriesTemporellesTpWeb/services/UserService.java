package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.DuplicateResourceException;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.ForbiddenActionException;
import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.RegisterUserInput;
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

    public boolean initiatorIsOwner(long userId, Long initiatorId){
        return userId == initiatorId;
    }

    // does the find work
    public User find(long userId){
        Optional<User> result = userRepository.findById(userId);
        // check that targeted user exists
        if (result.isEmpty()){
            throw new NotFoundException("'User' with id " + userId + " not found");
        }
        return result.get();
    }

    /*
        A user can only access his own data
         intiatorId: id of the user that initiated the request
         userId: id of the user to retrieve
     */
    public User find(long userId, Long intiatorId){
        // check that the initiator id corresponds to the targeted user's id
        if (!initiatorIsOwner(userId, intiatorId)){
            throw new ForbiddenActionException("You cannot access another user's data");
        }

        return find(userId);
    }

    public List<User> listUsers(){
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public User registerUser(RegisterUserInput user){

        Optional<User> _user = userRepository.findByUsername(user.getUsername());

        if (_user.isPresent()){
            throw new DuplicateResourceException("'User' with username " + user.getUsername() + " already exists");
        }

        return userRepository.save(new User(user.getUsername(), passwordEncoder.encode(user.getPassword())));

    }

}
