package com.amboucheba.seriesTemporellesTpWeb.repositories;

import com.amboucheba.seriesTemporellesTpWeb.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
