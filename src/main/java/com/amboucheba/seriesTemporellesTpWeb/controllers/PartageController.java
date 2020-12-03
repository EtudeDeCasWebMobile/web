package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.PartageList;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.PartageService;
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
    PartageService partageService;

    @GetMapping(value = "serietemporelle/{serieTemporelleId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAllBySerieTemporelleId(@PathVariable("serieTemporelleId") long serieTemporelleId){
        PartageList partages = partageService.listPartageBySerieTemporelleId(serieTemporelleId);
        return ResponseEntity.ok(partages);
    }

    @GetMapping(value = "user/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAllByUserId(@PathVariable("userId") long userId){
        PartageList partages = partageService.listPartageByUserId(userId);
        return ResponseEntity.ok(partages);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Partage created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Partage info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addPartage(@Valid @RequestBody Partage newPartage){
        long partageId = partageService.createPartage(newPartage);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(partageId)
                .toUri();

        return ResponseEntity.created(location).build();
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
        Partage partage = partageService.updatePartage(newPartage, partageId);


        return ResponseEntity.ok(partage);
    }


    @DeleteMapping(value = "/{partageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Partage deleted"),
            @ApiResponse(code = 404, message = "Partage not found")
    })
    public ResponseEntity<Void> deletePartage(@PathVariable("partageId") long partageId){

        partageService.removePartage(partageId);
        return ResponseEntity.noContent().build();
    }
}
