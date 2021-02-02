package com.amboucheba.etudeDeCasWeb.Repositories;

import com.amboucheba.etudeDeCasWeb.Models.Partage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PartageRepository extends CrudRepository<Partage, Long> {

    public List<Partage> findBySerieTemporelleId(long serieTemporelleId);
    public List<Partage> findByUserId(long userId);
    public Optional<Partage> findByUserIdAndSerieTemporelleIdAndType(long userId, long stId, String type);


}
