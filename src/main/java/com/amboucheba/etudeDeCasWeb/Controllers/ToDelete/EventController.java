package com.amboucheba.etudeDeCasWeb.Controllers.ToDelete;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Event;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists.EventList;
import com.amboucheba.etudeDeCasWeb.Services.ToDelete.EventService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping(
            value = "/seriestemporelles/{serieTemporelleId}/events",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Serie Temporelle not found"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<EventList> getEventsBySerieTemporelle(
            @PathVariable("serieTemporelleId") long serieTemporelleId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        List<Event> events = eventService.listEventsBySerieTemporelle(serieTemporelleId, userDetails.getUserId());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                    .maxAge(10, TimeUnit.SECONDS)
                    .cachePublic()
                    .proxyRevalidate()
                    .staleIfError(5, TimeUnit.SECONDS))
                .body(new EventList(events));
    }

    @PostMapping(
            value = "/seriestemporelles/{serieTemporelleId}/events",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Event created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Event info not valid, check response body for more details on error"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Serie Temporelle not found"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> addEventToSerieTemporelle(
            @PathVariable("serieTemporelleId") long serieTemporelleId,
            @Valid @RequestBody Event newEvent,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Event savedEvent = eventService.addEventToSerieTemporelle(serieTemporelleId, newEvent, userDetails.getUserId());

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("events", "{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(
            value = "/events/{eventId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event returned in body"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Event not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Event> getEventById(
            @PathVariable("eventId") long eventId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Event event = eventService.find(eventId, userDetails.getUserId());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                    .maxAge(10, TimeUnit.SECONDS)
                    .cachePublic()
                    .proxyRevalidate()
                    .staleIfError(5, TimeUnit.SECONDS))
                .body(event);
    }

    @PutMapping(
            value = "/events/{eventId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Event updated and returned in response body"),
            @ApiResponse(code = 400, message = "Provided Event info not valid, check response body for more details on error"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Event not found"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Event> updateEvent(
            @PathVariable("eventId") long eventId,
            @Valid @RequestBody Event newEvent,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Event updatedEvent = eventService.updateEvent(eventId, newEvent, userDetails.getUserId());
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping(value = "/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Event has been deleted"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Event not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable("eventId") long eventId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        eventService.remove(eventId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
