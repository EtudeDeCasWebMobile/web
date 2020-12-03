package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.Tag;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    EventService eventService;


    public List<Tag> listTagsByEvent(long eventId) {

        Event event = eventService.find(eventId);
        return tagRepository.findByEventId(eventId);
    }

    public Tag addTagToEvent(long eventId, Tag newTag) {

        Event event = eventService.find(eventId);
        newTag.setEvent(event);
        return tagRepository.save(newTag);
    }

    public Tag find(long tagId) {

        Optional<Tag> tag = tagRepository.findById(tagId);
        if (tag.isEmpty()){
            throw new NotFoundException("'Tag' with id " + tagId + " not found");
        }

        return tag.get();
    }

    public Tag updateTag(long tagId, Tag newTag) {
        Optional<Tag> tag = tagRepository.findById(tagId);

        if (tag.isEmpty()){
            throw new NotFoundException("'Tag' with id " + tagId + " not found");
        }

        Tag actualTag = tag.get();
        actualTag.setName(newTag.getName());
        return tagRepository.save(actualTag);
    }

    public void remove(long tagId) {

        if (!tagRepository.existsById(tagId)){
            throw new NotFoundException("'Tag' with id " + tagId + " not found");
        }
        tagRepository.deleteById(tagId);
    }
}
