package com.amboucheba.seriesTemporellesTpWeb.resources;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Evenement;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.repositories.EventRepository;
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
@RequestMapping("/evenements")
public class EvenementResource {

    @Autowired
    EventRepository eventRepository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAll(){

        List<Evenement> evenements = StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(evenements);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Event info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addEvent(@Valid @RequestBody Evenement newEvent){
        Evenement savedEvent = eventRepository.save(newEvent);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{eventId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event returned in body"),
            @ApiResponse(code = 404, message = "Event not found")
    })
    public ResponseEntity<Evenement> getEventById(@PathVariable("eventId") long eventId){

        Optional<Evenement> event = eventRepository.findById(eventId);
        if (event.isEmpty()){
            throw new NotFoundException("Tag with id " + eventId + " not found");
        }
        return ResponseEntity.ok(event.get());
    }

    @PutMapping(
            value = "/{eventId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event updated and returned in response body"),
            @ApiResponse(code = 201, message = "Event created,  check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Event info not valid, check response body for more details on error")
    })
    public ResponseEntity<Evenement> updateEvent(@PathVariable("eventId") long eventId, @Valid @RequestBody Evenement newEvent){
        Optional<Evenement> event = eventRepository.findById(eventId);

        if (event.isPresent()){
            Evenement actualEvent = event.get();
            actualEvent.setDate(newEvent.getDate());
            actualEvent.setValeur(newEvent.getValeur());
            actualEvent.setCommentaire(newEvent.getCommentaire());
            Evenement savedEvent = eventRepository.save(actualEvent);
            return ResponseEntity.ok(savedEvent);
        }

        // Event does not exist so create it
        Evenement savedEvent = eventRepository.save(newEvent);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("evenements")
                .path("/{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping(value = "/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Event deleted"),
            @ApiResponse(code = 404, message = "Event not found")
    })
    public ResponseEntity<Void> deleteEvent(@PathVariable("eventId") long eventId){

        if (!eventRepository.existsById(eventId)){
            throw new NotFoundException("Message with id " + eventId + " not found");
        }
        eventRepository.deleteById(eventId);

        return ResponseEntity.noContent().build();
    }
}
