package com.amboucheba.etudeDeCasWeb.Services.ToDelete;

import com.amboucheba.etudeDeCasWeb.Exceptions.ForbiddenActionException;
import com.amboucheba.etudeDeCasWeb.Exceptions.NotFoundException;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.SerieTemporelle;
import com.amboucheba.etudeDeCasWeb.Repositories.ToDelete.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    SerieTemporelleService serieTemporelleService;

    @Autowired
    UsersService usersService;

    @Autowired
    PartageService partageService;

    List<Event> listEventsBySerieTemporelle(long serieTemporelleId){

        return eventRepository.findBySerieTemporelleId(serieTemporelleId);
    }
    public List<Event> listEventsBySerieTemporelle(long serieTemporelleId, Long initiatorId){

        SerieTemporelle st = serieTemporelleService.find(serieTemporelleId);

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, st.getId(), "r");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return listEventsBySerieTemporelle(serieTemporelleId);
    }

    public Event find( long eventId){

        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()){
            throw new NotFoundException("'Event' with id " + eventId + " not found.");
        }
        return event.get();
    }
    public Event find( long eventId, Long initiatorId){

        Event event = find(eventId);
        SerieTemporelle st = event.getSerieTemporelle();

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, st.getId(), "r");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return event;
    }

    Event addEventToSerieTemporelle(SerieTemporelle serieTemporelle, Event event){

        event.setSerieTemporelle(serieTemporelle);
        return eventRepository.save(event);
    }
    public Event addEventToSerieTemporelle(long serieTemporelleId, Event event, Long initiatorId){

        // look for the serie temporelle
        SerieTemporelle st = serieTemporelleService.find(serieTemporelleId);

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, st.getId(), "w");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return addEventToSerieTemporelle(st, event);
    }


    Event updateEvent(Event event, Event newEvent) {

        event.setDate(newEvent.getDate());
        event.setValeur(newEvent.getValeur());
        event.setCommentaire(newEvent.getCommentaire());
        return eventRepository.save(event);
    }
    public Event updateEvent(long eventId, Event newEvent, Long initiatorId) {

        // handles not found
        Event event = find(eventId);
        SerieTemporelle st = event.getSerieTemporelle();

        // initiator is the owner of the targeted serie temporelle
        boolean isOwner = usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId);
        // Owner shared serie temporelle with initiator with read(r) access
        boolean hasAccess = partageService.hasAccess(initiatorId, st.getId(), "w");

        if (!isOwner && !hasAccess){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }

        return updateEvent(event, newEvent);
    }

    void remove(long eventId) {
        eventRepository.deleteById(eventId);
    }
    public void remove(long eventId, Long initiatorId) {

        Event event = find(eventId);
        SerieTemporelle st = event.getSerieTemporelle();

        // initiator of the request must the owner of the serie temporelle
        if (!usersService.initiatorIsOwner(st.getOwner().getId(), initiatorId)){
            throw new ForbiddenActionException("Permission denied: cannot access another user's data");
        }
        remove(eventId);
    }
}
