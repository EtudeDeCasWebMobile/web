package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.models.User;
import com.amboucheba.seriesTemporellesTpWeb.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


//    public List<User>

    public long registerUser(User user){
        return userRepository.save(user).getId();
    }


}
