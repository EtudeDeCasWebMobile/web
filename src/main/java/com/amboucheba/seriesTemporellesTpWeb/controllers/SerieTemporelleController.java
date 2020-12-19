package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.AuthDetails;
import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.SerieTemplorelleList;
import com.amboucheba.seriesTemporellesTpWeb.models.SerieTemporelle;
import com.amboucheba.seriesTemporellesTpWeb.services.SerieTemporelleService;
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
public class SerieTemporelleController {

    @Autowired
    SerieTemporelleService serieTemporelleService;

    // Should be removed -> or change to series temporelle to which user has access(owned + shared)
    @GetMapping(value = "/seriesTemporelles", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<SerieTemplorelleList> getAll(){

        SerieTemplorelleList liste = serieTemporelleService.listSerieTemporelle();
        return ResponseEntity.ok(liste);
    }

    @PostMapping(
            value = "/users/{userId}/seriesTemporelles",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "SerieTemporelle created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "User not found"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<SerieTemporelle> addSerieTemporelle(
            @Valid @RequestBody SerieTemporelle newSerieTemporelle,
            @PathVariable long userId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){
        long createdSerieTemporelleId = serieTemporelleService.createSerieTemporelle(newSerieTemporelle, userId, userDetails.getUserId()).getId();

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("seriesTemporelles", "{id}")
                .buildAndExpand(createdSerieTemporelleId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(
            value = "/users/{userId}/seriesTemporelles",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<SerieTemplorelleList> getAllOfOwner(
            @PathVariable long userId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        List<SerieTemporelle> list = serieTemporelleService.listSerieTemporelleOfOwner(userId, userDetails.getUserId());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(30, TimeUnit.SECONDS)
                        .cachePrivate()
                        .mustRevalidate())
                .body(new SerieTemplorelleList(list));
    }


    @GetMapping(
            value = "/seriesTemporelles/{serieTemporelleId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle returned in body"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<SerieTemporelle> getSerieTemporelleById(
            @PathVariable long serieTemporelleId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        SerieTemporelle serieTemporelle = serieTemporelleService.find(serieTemporelleId, userDetails.getUserId());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(10, TimeUnit.SECONDS)
                        .cachePublic()
                        .proxyRevalidate()
                        .staleIfError(5, TimeUnit.SECONDS))
                .body(serieTemporelle);
    }

    @PutMapping(
            value = "/seriesTemporelles/{serieTemporelleId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "SerieTemporelle updated and returned in response body"),
            @ApiResponse(code = 400, message = "Provided SerieTemporelle info not valid, check response body for more details on error"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<SerieTemporelle> updateSerieTemporelle(
            @PathVariable long serieTemporelleId,
            @Valid @RequestBody SerieTemporelle newSerieTemporelle,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        SerieTemporelle modifiedSerieTemporelle = serieTemporelleService.updateSerieTemporelle(
                newSerieTemporelle, serieTemporelleId, userDetails.getUserId()
        );
        return ResponseEntity.ok(modifiedSerieTemporelle);
    }

    @DeleteMapping(value = "/seriesTemporelles/{serieTemporelleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "SerieTemporelle deleted"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "SerieTemporelle not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> deleteSerieTemporelle(
            @PathVariable long serieTemporelleId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        serieTemporelleService.removeSerieTemporelle(serieTemporelleId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
