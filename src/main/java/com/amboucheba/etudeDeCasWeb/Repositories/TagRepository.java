package com.amboucheba.etudeDeCasWeb.Repositories;

import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Tag;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TagRepository extends CrudRepository<Tag, Long> {

    public List<Tag> findByEventId(long eventId);
}
