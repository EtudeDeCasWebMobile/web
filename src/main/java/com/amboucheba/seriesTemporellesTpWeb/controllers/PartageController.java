package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
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
@RequestMapping("/partages")
public class PartageController {

    @Autowired
    PartageRepository partageRepository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAll(){
        List<Partage> partages = StreamSupport.stream(partageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(partages);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Partage created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Partage info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addPartage(@Valid @RequestBody Partage newPartage){
        Partage savedPartage = partageRepository.save(newPartage);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPartage.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{partageId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partage returned in body"),
            @ApiResponse(code = 404, message = "Partage not found")
    })
    public ResponseEntity<Partage> getPartageById(@PathVariable("partageId") long partageId){

        Optional<Partage> partage = partageRepository.findById(partageId);
        if (partage.isEmpty()){
            throw new NotFoundException("Partage with id " + partageId + " not found");
        }
        return ResponseEntity.ok(partage.get());
    }

    @PutMapping(
            value = "/{partageId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partage updated and returned in response body"),
            @ApiResponse(code = 201, message = "Partage created,  check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Partage info not valid, check response body for more details on error")
    })
    public ResponseEntity<Partage> updatePartage(@PathVariable("partageId") long partageId, @Valid @RequestBody Partage newPartage){
        Optional<Partage> partage = partageRepository.findById(partageId);

        if (partage.isPresent()){
            Partage actualPartage = partage.get();
            actualPartage.setType(newPartage.getType());
            Partage savedPartage = partageRepository.save(actualPartage);
            return ResponseEntity.ok(savedPartage);
        }

        // Partage does not exist so create it
        Partage savedEvent = partageRepository.save(newPartage);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("partages")
                .path("/{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping(value = "/{partageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Partage deleted"),
            @ApiResponse(code = 404, message = "Partage not found")
    })
    public ResponseEntity<Void> deletePartage(@PathVariable("partageId") long partageId){

        if (!partageRepository.existsById(partageId)){
            throw new NotFoundException("Partage with id " + partageId + " not found");
        }
        partageRepository.deleteById(partageId);

        return ResponseEntity.noContent().build();
    }
}
