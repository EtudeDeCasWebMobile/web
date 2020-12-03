package com.amboucheba.seriesTemporellesTpWeb.repositories;

import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SerieTemporelleRepository extends CrudRepository<SerieTemporelle, Long> {
    public List<SerieTemporelle> findByOwnerId(long userId);

}
