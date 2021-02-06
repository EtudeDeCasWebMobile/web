package com.amboucheba.etudeDeCasWeb.Repositories.ToDelete;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    public List<Event> findBySerieTemporelleId(long serieTemporelleId);

}
