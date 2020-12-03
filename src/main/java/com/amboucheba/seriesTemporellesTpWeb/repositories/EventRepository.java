package com.amboucheba.seriesTemporellesTpWeb.repositories;

import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    public List<Event> findBySerieTemporelleId(long serieTemporelleId);
}
