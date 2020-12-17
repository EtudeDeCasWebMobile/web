package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.AuthDetails;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.PartageList;
import com.amboucheba.seriesTemporellesTpWeb.models.Partage;
import com.amboucheba.seriesTemporellesTpWeb.models.PartageRequest;
import com.amboucheba.seriesTemporellesTpWeb.repositories.PartageRepository;
import com.amboucheba.seriesTemporellesTpWeb.services.PartageService;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class PartageController {

    @Autowired
    PartageService partageService;

    // is this endpoint really useful
    @GetMapping(
            value = "/partages/{partageId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Partage not found")
    })
    public ResponseEntity<Partage> getPartageById(@PathVariable long partageId, @AuthenticationPrincipal AuthDetails userDetails){
        Partage partage = partageService.find(partageId, userDetails.getUserId());
        return ResponseEntity.ok(partage);
    }

    @GetMapping(
            value = "/seriesTemporelles/{serieTemporelleId}/partages",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Serie Temporelle not found") })
    public ResponseEntity<PartageList> getAllBySerieTemporelleId(
            @PathVariable long serieTemporelleId, @AuthenticationPrincipal AuthDetails userDetails){

        List<Partage> partages = partageService.listPartageBySerieTemporelleId(serieTemporelleId, userDetails.getUserId());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(10, TimeUnit.SECONDS)
                        .cachePrivate()
                        .mustRevalidate())
                .body(new PartageList(partages));
    }

    @GetMapping(
            value = "/users/{userId}/partages",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "User not found") })
    public ResponseEntity<PartageList> getAllByUserId(@PathVariable long userId, @AuthenticationPrincipal AuthDetails userDetails){

        List<Partage> partages = partageService.listPartageByUserId(userId, userDetails.getUserId());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(10, TimeUnit.SECONDS)
                        .cachePrivate()
                        .mustRevalidate())
                .body(new PartageList(partages));
    }

    @PostMapping(
            value = "/partages",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Partage created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Partage info not valid, check response body for more details on error"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "User or Serie Temporelle not found")
    })
    public ResponseEntity<Void> addPartage(
            @Valid @RequestBody PartageRequest newPartage, @AuthenticationPrincipal AuthDetails userDetails
    ){
        Partage partage = partageService.createPartage(newPartage, userDetails.getUserId());

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("partages", "{id}")
                .buildAndExpand(partage.getId())
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
            @ApiResponse(code = 400, message = "Provided Partage info not valid, check response body for more details on error"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Partage not found")
    })
    public ResponseEntity<Partage> updatePartage(
            @PathVariable long partageId,
            @Valid @RequestBody PartageRequest newPartage,
            @AuthenticationPrincipal AuthDetails userDetails){

        Partage partage = partageService.updatePartage(newPartage, partageId, userDetails.getUserId());
        return ResponseEntity.ok(partage);
    }

    @DeleteMapping(value = "/partages/{partageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Partage deleted"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Partage not found")
    })
    public ResponseEntity<Void> deletePartage(@PathVariable long partageId, @AuthenticationPrincipal AuthDetails userDetails){

        partageService.removePartage(partageId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
