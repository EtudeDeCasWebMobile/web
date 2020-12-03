package com.amboucheba.seriesTemporellesTpWeb.repositories;

import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SerieTemporelleRepository extends CrudRepository<SerieTemporelle, Long> {
    public List<SerieTemporelle> findByOwnerId(long userId);

}
