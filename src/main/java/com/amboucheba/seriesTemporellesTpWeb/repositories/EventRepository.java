package com.amboucheba.seriesTemporellesTpWeb.repositories;

import com.amboucheba.seriesTemporellesTpWeb.models.Evenement;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Evenement, Long> {
}
