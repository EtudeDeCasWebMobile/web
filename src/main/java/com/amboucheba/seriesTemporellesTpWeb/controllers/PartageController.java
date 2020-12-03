package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.PartageList;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.PartageRequest;
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
public class PartageController {

    @Autowired
    PartageService partageService;

    @GetMapping(
            value = "/seriesTemporelles/{serieTemporelleId}/partages",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Serie Temporelle not found") })
    public ResponseEntity<PartageList> getAllBySerieTemporelleId(@PathVariable long serieTemporelleId){
        List<Partage> partages = partageService.listPartageBySerieTemporelleId(serieTemporelleId);
        return ResponseEntity.ok(new PartageList(partages));
    }

    @GetMapping(
            value = "/users/{userId}/partages",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "User not found") })
    public ResponseEntity<PartageList> getAllByUserId(@PathVariable long userId){

        List<Partage> partages = partageService.listPartageByUserId(userId);
        return ResponseEntity.ok(new PartageList(partages));
    }

    @PostMapping(
            value = "/partages",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Partage created, check location header for uri"),
            @ApiResponse(code = 404, message = "User or Serie Temporelle not found"),
            @ApiResponse(code = 400, message = "Provided Partage info not valid, check response body for more details on error") })
    public ResponseEntity<Void> addPartage(@Valid @RequestBody PartageRequest newPartage){
        long partageId = partageService.createPartage(newPartage);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(partageId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(
            value = "/partages/{partageId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Partage updated and returned in response body"),
            @ApiResponse(code = 404, message = "Partage not found"),
            @ApiResponse(code = 400, message = "Provided Partage info not valid, check response body for more details on error")
    })
    public ResponseEntity<Partage> updatePartage(@PathVariable long partageId, @Valid @RequestBody PartageRequest newPartage){

        Partage partage = partageService.updatePartage(newPartage, partageId);
        return ResponseEntity.ok(partage);
    }

    @DeleteMapping(value = "/partages/{partageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Partage deleted"),
            @ApiResponse(code = 404, message = "Partage not found")
    })
    public ResponseEntity<Void> deletePartage(@PathVariable long partageId){

        partageService.removePartage(partageId);
        return ResponseEntity.noContent().build();
    }
}
