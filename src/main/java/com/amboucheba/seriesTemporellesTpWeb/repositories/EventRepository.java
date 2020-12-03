package com.amboucheba.seriesTemporellesTpWeb.repositories;

import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<Event, Long> {

    public List<Event> findBySerieTemporelleId(long serieTemporelleId);

    public Optional<Event> findByIdAndSerieTemporelleId(long eventId, long serieTemporelleId);
}
