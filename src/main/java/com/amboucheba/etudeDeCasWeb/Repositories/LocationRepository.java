package com.amboucheba.etudeDeCasWeb.Repositories;

import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, Long> {

	Optional<Location> findByOwner(User user);



}
