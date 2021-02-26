package com.amboucheba.etudeDeCasWeb.Repositories;

import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CollectionRepository extends CrudRepository<Collection, Long> {

    List<Collection> findByOwnerId(long ownerId);

    Optional<Collection> findByTagAndOwnerId(String tag, long ownerId);
}
