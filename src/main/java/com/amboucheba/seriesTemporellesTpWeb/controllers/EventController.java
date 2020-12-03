package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Event;
import com.amboucheba.seriesTemporellesTpWeb.models.EventList;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.EventService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping(
            value = "/seriesTemporelles/{serieTemporelleId}/events",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<EventList> getEventsBySerieTemporelle(@PathVariable("serieTemporelleId") long serieTemporelleId){

        List<Event> events = eventService.listEventsBySerieTemporelle(serieTemporelleId);

        return ResponseEntity.ok(new EventList(events));
    }

    @PostMapping(
            value = "/seriesTemporelles/{serieTemporelleId}/events",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Event info not valid, check response body for more details on error") })
    public ResponseEntity<Void> addEventToSerieTemporelle(
            @PathVariable("serieTemporelleId") long serieTemporelleId,
            @Valid @RequestBody Event newEvent){
        Event savedEvent = eventService.addEventToSerieTemporelle(serieTemporelleId, newEvent);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(
            value = "/events/{eventId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event returned in body"),
            @ApiResponse(code = 404, message = "Event not found") })
    public ResponseEntity<Event> getEventById(@PathVariable("eventId") long eventId){

        Event event = eventService.find(eventId);
        return ResponseEntity.ok(event);
    }

    @PutMapping(
            value = "/events/{eventId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event updated and returned in response body"),
            @ApiResponse(code = 400, message = "Provided Event info not valid, check response body for more details on error")
    })
    public ResponseEntity<Event> updateEvent(@PathVariable("eventId") long eventId, @Valid @RequestBody Event newEvent){

        Event updatedEvent = eventService.updateEvent(eventId, newEvent);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping(value = "/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Event has been deleted"),
            @ApiResponse(code = 404, message = "Event not found")
    })
    public ResponseEntity<Void> deleteEvent(@PathVariable("eventId") long eventId){

        eventService.remove(eventId);
        return ResponseEntity.noContent().build();
    }
}
