package com.amboucheba.etudeDeCasWeb.Services;

import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Tag;
import com.amboucheba.etudeDeCasWeb.Repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    EventService eventService;

    @Autowired
    UserService userService;

    @Autowired
    PartageService partageService;

    Tag find(long tagId) {

        Optional<Tag> tag = tagRepository.findById(tagId);
        if (tag.isEmpty()){
            throw new NotFoundException("'Tag' with id " + tagId + " not found");
        }

        return tag.get();
    }
    public Tag find(long tagId, Long initiatorId) {

        Tag tag = find(tagId);
        SerieTemporelle st = tag.getEvent().getSerieTemporelle();

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = userService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, st.getId(), "r");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return tag;
    }

    List<Tag> listTagsByEvent(long eventId) {
        return tagRepository.findByEventId(eventId);
    }
    public List<Tag> listTagsByEvent(long eventId, Long initiatorId) {

        Event event = eventService.find(eventId);
        SerieTemporelle st = event.getSerieTemporelle();

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = userService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, st.getId(), "r");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return listTagsByEvent(eventId);
    }

    Tag addTagToEvent(Event event, Tag newTag) {

        newTag.setEvent(event);
        return tagRepository.save(newTag);
    }
    public Tag addTagToEvent(long eventId, Tag newTag, Long initiatorId) {

        Event event = eventService.find(eventId);
        SerieTemporelle st = event.getSerieTemporelle();

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = userService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, st.getId(), "w");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return addTagToEvent(event, newTag);
    }

    Tag updateTag(Tag tag, Tag newTag) {

        tag.setName(newTag.getName());
        return tagRepository.save(tag);
    }
    public Tag updateTag(long tagId, Tag newTag, Long initiatorId) {
        Tag tag = find(tagId);
        SerieTemporelle st = tag.getEvent().getSerieTemporelle();

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = userService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, st.getId(), "w");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return updateTag(tag, newTag);
    }

    void remove(long tagId) {
        tagRepository.deleteById(tagId);
    }
    public void remove(long tagId, Long initiatorId) {

        Tag tag = find(tagId);
        SerieTemporelle st = tag.getEvent().getSerieTemporelle();

        // initiator of the request must the owner of the serie temporelle
        if (!userService.initiatorIsOwner(st.getOwner().getId(), initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        remove(tagId);
    }
}
