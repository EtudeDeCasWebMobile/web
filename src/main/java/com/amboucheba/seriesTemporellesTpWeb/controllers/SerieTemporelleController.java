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

    @GetMapping(value = "/seriestemporelles", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAll(){

        SerieTemplorelleList liste = serieTemporelleService.listSerieTemporelle();

        return ResponseEntity.ok(liste);
    }

    @GetMapping(
            value = "/users/{userId}/seriestemporelles",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "User not found") })
    public ResponseEntity<SerieTemplorelleList> getAllOfOwner(@PathVariable long userId){

        List<SerieTemporelle> list = serieTemporelleService.listSerieTemporelleOfOwner(userId);
        return ResponseEntity.ok(new SerieTemplorelleList(list));
    }

    @PostMapping(
            value = "/users/{userId}/seriestemporelles",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "SerieTemporelle created, check location header for uri"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error")
    })
    public ResponseEntity<SerieTemporelle> addSerieTemporelle(@Valid @RequestBody SerieTemporelle newSerieTemporelle, @PathVariable long userId){
        long createdSerieTemporelleId = serieTemporelleService.createSerieTemporelle(newSerieTemporelle, userId).getId();

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSerieTemporelleId)
                .toUri();

        return new ResponseEntity(newSerieTemporelle, HttpStatus.CREATED);
    }

    @GetMapping(
            value = "/seriestemporelles/{serieTemporelleId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle returned in body"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found") })
    public ResponseEntity<SerieTemporelle> getSerieTemporelleById(@PathVariable long serieTemporelleId){

        SerieTemporelle serieTemporelle = serieTemporelleService.find(serieTemporelleId);
        return ResponseEntity.ok(serieTemporelle);
    }


    @PutMapping(
            value = "/seriestemporelles/{serieTemporelleId}",
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

    @DeleteMapping(value = "/seriestemporelles/{serieTemporelleId}")
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
