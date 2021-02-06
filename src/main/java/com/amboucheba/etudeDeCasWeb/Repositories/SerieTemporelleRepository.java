package com.amboucheba.etudeDeCasWeb.Repositories;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SerieTemporelleRepository extends CrudRepository<SerieTemporelle, Long> {
    public List<SerieTemporelle> findByOwnerId(long userId);

}
