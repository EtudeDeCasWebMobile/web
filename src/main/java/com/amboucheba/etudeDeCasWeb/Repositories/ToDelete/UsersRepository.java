package com.amboucheba.etudeDeCasWeb.Repositories.ToDelete;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
}
