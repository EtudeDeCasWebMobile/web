package com.amboucheba.soatp2.repositories;

import com.amboucheba.soatp2.models.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository extends CrudRepository<Message, Long> {

    List<Message> findByUsername(String username);
}
