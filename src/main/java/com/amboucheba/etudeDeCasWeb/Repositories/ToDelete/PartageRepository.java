package com.amboucheba.etudeDeCasWeb.Repositories.ToDelete;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Partage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PartageRepository extends CrudRepository<Partage, Long> {

    public List<Partage> findBySerieTemporelleId(long serieTemporelleId);
    public List<Partage> findByUsersId(long userId);
    public Optional<Partage> findByUsersIdAndSerieTemporelleIdAndType(long userId, long stId, String type);


}
