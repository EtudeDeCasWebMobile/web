package com.amboucheba.etudeDeCasWeb.Repositories;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
}
