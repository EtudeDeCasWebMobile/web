package com.amboucheba.seriesTemporellesTpWeb.repositories;

import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByUsername(String username);
}
