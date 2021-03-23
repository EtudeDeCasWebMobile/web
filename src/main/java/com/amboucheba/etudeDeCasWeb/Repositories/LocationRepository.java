package com.amboucheba.etudeDeCasWeb.Repositories;

import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findByOwnerId(long ownerId);
    Optional<Location> findByTitleAndOwnerId(String title, long ownerId);
}
