package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemplorelleList;
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

@RestController
@RequestMapping("/serieTemporelles")
public class SerieTemporelleController {

    @Autowired
    SerieTemporelleService serieTemporelleService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAll(){

        SerieTemplorelleList liste = serieTemporelleService.listSerieTemporelle();

        return ResponseEntity.ok(liste);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "SerieTemporelle created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addSerieTemporelle(@Valid @RequestBody SerieTemporelle newSerieTemporelle){
        long createdSerieTemporelleId = serieTemporelleService.createSerieTemporelle(newSerieTemporelle);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSerieTemporelleId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{serieTemporelleId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle returned in body"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found")
    })
    public ResponseEntity<SerieTemporelle> getSerieTemporelleById(@PathVariable("serieTemporelleId") long serieTemporelleId) throws NotFoundException{

        SerieTemporelle serieTemporelle = serieTemporelleService.getSerieTemporelleById(serieTemporelleId);
        return ResponseEntity.ok(serieTemporelle);
    }


    @PutMapping(
            value = "/{serieTemporelleId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle updated and returned in response body"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error")
    })
    public ResponseEntity<SerieTemporelle> updateSerieTemporelle(@PathVariable("serieTemporelleId") long serieTemporelleId, @Valid @RequestBody SerieTemporelle newSerieTemporelle) throws  NotFoundException{

        SerieTemporelle modifiedSerieTemporelle = serieTemporelleService.updateSerieTemporelle(newSerieTemporelle, serieTemporelleId);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("serieTemporelles")
                .path("/{id}")
                .buildAndExpand(modifiedSerieTemporelle.getId())
                .toUri();

        return ResponseEntity.ok(modifiedSerieTemporelle);
    }


    @DeleteMapping(value = "/{serieTemporelleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "SerieTemporelle deleted"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found")
    })
    public ResponseEntity<Void> deleteSerieTemporelle(@PathVariable("serieTemporelleId") long serieTemporelleId){

        serieTemporelleService.removeSerieTemporelle(serieTemporelleId);

        return ResponseEntity.noContent().build();
    }
}
