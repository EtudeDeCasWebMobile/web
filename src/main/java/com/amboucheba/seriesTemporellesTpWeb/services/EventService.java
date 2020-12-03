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


    public List<Event> listEventsBySerieTemporelle(long serieTemporelleId){
        return eventRepository.findBySerieTemporelleId(serieTemporelleId);
    }

    public Event find(long eventId){
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()){
            throw new NotFoundException("Event with id " + eventId + " not found");
        }
        return event.get();
    }

    public Event addEventToSerieTemporelle(long serieTemporelleId, Event event){
//        return userRepository.save(user);
        return null;
    }
}
