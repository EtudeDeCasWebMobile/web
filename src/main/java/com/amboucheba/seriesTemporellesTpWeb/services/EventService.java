package com.amboucheba.seriesTemporellesTpWeb.services;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
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

    public List<Event> listEventsBySerieTemporelle(long serieTemporelleId){

        SerieTemporelle serieTemporelle = serieTemporelleService.find(serieTemporelleId);
        return eventRepository.findBySerieTemporelleId(serieTemporelleId);
    }

    public Event find( long eventId){

        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()){
            throw new NotFoundException("'Event' with id " + eventId + " not found.");
        }
        return event.get();
    }

    public Event addEventToSerieTemporelle(long serieTemporelleId, Event event){

        // look for the serie temporelle
        SerieTemporelle serieTemporelle = serieTemporelleService.find(serieTemporelleId);

        // serie temporelle exists
        event.setSerieTemporelle(serieTemporelle);
        return eventRepository.save(event);
    }

    public Event updateEvent(long eventId, Event newEvent) {
        Optional<Event> event = eventRepository.findById(eventId);

        if (event.isEmpty()){
            throw new NotFoundException("'Event' with id:" + eventId + " not found.");
        }

        Event actualEvent = event.get();
        actualEvent.setDate(newEvent.getDate());
        actualEvent.setValeur(newEvent.getValeur());
        actualEvent.setCommentaire(newEvent.getCommentaire());
        return eventRepository.save(actualEvent);
    }

    public void remove(long eventId) {
        if (!eventRepository.existsById(eventId)){
            throw new NotFoundException("'Event' with id " + eventId + " not found");
        }
        eventRepository.deleteById(eventId);
    }
}
