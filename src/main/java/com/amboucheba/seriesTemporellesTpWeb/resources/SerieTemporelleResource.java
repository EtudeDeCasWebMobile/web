package com.amboucheba.seriesTemporellesTpWeb.resources;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.repositories.SerieTemporelleRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/serieTemporelles")
public class SerieTemporelleResource {

    @Autowired
    SerieTemporelleRepository serieTemporelleRepository;
    @GetMapping
    public ResponseEntity getAll(){

        List<SerieTemporelle> serieTemporelles = StreamSupport.stream(serieTemporelleRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(serieTemporelles);
    }

    @PostMapping(consumes = "application/json" )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "SerieTemporelle created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addSerieTemporelle(@Valid @RequestBody SerieTemporelle newSerieTemporelle){
        SerieTemporelle savedSerieTemporelle = serieTemporelleRepository.save(newSerieTemporelle);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSerieTemporelle.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{serieTemporelleId}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle returned in body"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found")
    })
    public ResponseEntity<SerieTemporelle> getSerieTemporelleById(@PathVariable("serieTemporelleId") long serieTemporelleId){

        Optional<SerieTemporelle> serieTemporelle = serieTemporelleRepository.findById(serieTemporelleId);
        if (serieTemporelle.isEmpty()){
            throw new NotFoundException("SerieTemporelle with id " + serieTemporelleId + " not found");
        }
        return ResponseEntity.ok(serieTemporelle.get());
    }

    @PutMapping(value = "/{serieTemporelleId}", consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle updated and returned in response body"),
            @ApiResponse(code = 201, message = "SerieTemporelle created,  check location header for uri"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error")
    })
    public ResponseEntity<SerieTemporelle> updateSerieTemporelle(@PathVariable("serieTemporelleId") long serieTemporelleId, @Valid @RequestBody SerieTemporelle newSerieTemporelle){
        Optional<SerieTemporelle> serieTemporelle = serieTemporelleRepository.findById(serieTemporelleId);

        if (serieTemporelle.isPresent()){
            SerieTemporelle actualSerieTemporelle = serieTemporelle.get();
            actualSerieTemporelle.setTitre(newSerieTemporelle.getTitre());
            actualSerieTemporelle.setDescription(newSerieTemporelle.getDescription());
            SerieTemporelle savedSerieTemporelle = serieTemporelleRepository.save(actualSerieTemporelle);
            return ResponseEntity.ok(savedSerieTemporelle);
        }

        // Serie Temporelle does not exist so create it
        SerieTemporelle savedEvent = serieTemporelleRepository.save(newSerieTemporelle);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("serieTemporelles")
                .path("/{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping(value = "/{serieTemporelleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "SerieTemporelle deleted"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found")
    })
    public ResponseEntity<Void> deleteSerieTemporelle(@PathVariable("serieTemporelleId") long serieTemporelleId){

        if (!serieTemporelleRepository.existsById(serieTemporelleId)){
            throw new NotFoundException("SerieTemporelle with id " + serieTemporelleId + " not found");
        }
        serieTemporelleRepository.deleteById(serieTemporelleId);

        return ResponseEntity.noContent().build();
    }
}
