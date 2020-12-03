package com.amboucheba.seriesTemporellesTpWeb.repositories;

import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PartageRepository extends CrudRepository<Partage, Long> {
    public List<Partage> findBySerieTemporelleId(long serieTemporelleId);
    public List<Partage> findByUserId(long userId);

}
