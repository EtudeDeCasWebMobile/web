package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.SerieTemplorelleList;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
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

@RestController
public class SerieTemporelleController {

    @Autowired
    SerieTemporelleService serieTemporelleService;

    @GetMapping(value = "/seriesTemporelles", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SerieTemplorelleList> getAll(){

        SerieTemplorelleList liste = serieTemporelleService.listSerieTemporelle();
        return ResponseEntity.ok(liste);
    }

    @GetMapping(
            value = "/users/{userId}/seriesTemporelles",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "User not found") })
    public ResponseEntity<SerieTemplorelleList> getAllOfOwner(@PathVariable long userId){

        List<SerieTemporelle> list = serieTemporelleService.listSerieTemporelleOfOwner(userId);
        return ResponseEntity.ok(new SerieTemplorelleList(list));
    }

    @PostMapping(
            value = "/users/{userId}/seriesTemporelles",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "SerieTemporelle created, check location header for uri"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error")
    })

    public ResponseEntity<Void> addSerieTemporelleToUser(@Valid @RequestBody SerieTemporelle newSerieTemporelle, @PathVariable long userId){
        SerieTemporelle createdSerieTemporelle = serieTemporelleService.createSerieTemporelle(newSerieTemporelle, userId);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("seriesTemporelles", "{id}")
                .buildAndExpand(createdSerieTemporelle.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(
            value = "/seriesTemporelles/{serieTemporelleId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle returned in body"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found") })
    public ResponseEntity<SerieTemporelle> getSerieTemporelleById(@PathVariable long serieTemporelleId){

        SerieTemporelle serieTemporelle = serieTemporelleService.find(serieTemporelleId);
        return ResponseEntity.ok(serieTemporelle);
    }

    @PutMapping(
            value = "/seriesTemporelles/{serieTemporelleId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle updated and returned in response body"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error")
    })
    public ResponseEntity<SerieTemporelle> updateSerieTemporelle(
            @PathVariable long serieTemporelleId,
            @Valid @RequestBody SerieTemporelle newSerieTemporelle){

        SerieTemporelle modifiedSerieTemporelle = serieTemporelleService.updateSerieTemporelle(newSerieTemporelle, serieTemporelleId);
        return ResponseEntity.ok(modifiedSerieTemporelle);
    }

    @DeleteMapping(value = "/seriesTemporelles/{serieTemporelleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "SerieTemporelle deleted"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found")
    })
    public ResponseEntity<Void> deleteSerieTemporelle(@PathVariable long serieTemporelleId){

        serieTemporelleService.removeSerieTemporelle(serieTemporelleId);
        return ResponseEntity.noContent().build();
    }
}
