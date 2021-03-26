package com.amboucheba.etudeDeCasWeb.Controllers;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.AddTagToLocationInput;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.LocationInput;
import com.amboucheba.etudeDeCasWeb.Models.Outputs.Locations;
import com.amboucheba.etudeDeCasWeb.Services.LocationService;
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
import java.util.concurrent.TimeUnit;

@RestController
public class LocationController {


    @Autowired
    LocationService locationService;
    // Get my locations
    @GetMapping(
            value = "/me/locations",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
            })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "locations returned in body"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Locations> getUserLocations(@ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Locations locations = new Locations(locationService.listLocationsByUser(userDetails.getUserId()));

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(60, TimeUnit.SECONDS)
                        .cachePrivate()
                        .noTransform()
                        .staleIfError(1, TimeUnit.HOURS))
                .body(locations);
    }

    @PostMapping(
            value = "/me/locations",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "location created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided location info not valid, check response body for more details on error"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "A location with the same title  already exists for this user"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> addLocation(
            @Valid @RequestBody LocationInput newLocation,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        long createdLocationId = locationService.createLocation(newLocation, userDetails.getUserId()).getId();

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("locations", "{id}")
                .buildAndExpand(createdLocationId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping(
            value = "/locations/{locationId}/collections",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "location created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided location info not valid, check response body for more details on error"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 403, message = "User unauthorized: cannot update other users locations"),
            @ApiResponse(code = 404, message = "Location not found"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Collection> addTagToLocation(
            @PathVariable long locationId,
            @Valid @RequestBody AddTagToLocationInput input,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Collection collection = locationService.addToCollection(locationId, input.getTag(), userDetails.getUserId() );
        return ResponseEntity.ok(collection);
    }

    @PutMapping(
            value = "/locations/{locationId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "location updated and returned in response body"),
            @ApiResponse(code = 400, message = "Provided location info not valid, check response body for more details on error"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 404, message = "location not found"),
            @ApiResponse(code = 409, message = "A location with the same title name already exists for this user"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Location> updateLocation(
            @PathVariable long locationId,
            @Valid @RequestBody LocationInput newLocation,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Location modifiedLocation = locationService.updateLocation(
                newLocation, locationId, userDetails.getUserId()
        );
        return ResponseEntity.ok(modifiedLocation);
    }

    @DeleteMapping(value = "/locations/{locationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Location deleted"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 403, message = "Permission denied: Only owner of the location can delete it"),
            @ApiResponse(code = 404, message = "Location not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> deleteLocation(
            @PathVariable long locationId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        locationService.deleteLocation(locationId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/locations/{locationId}/collections")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Location deleted"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 403, message = "Permission denied: Only owner of the location can delete it"),
            @ApiResponse(code = 404, message = "Location not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> deleteTagFromLocation(
            @PathVariable long locationId,
            @Valid @RequestBody AddTagToLocationInput input,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        locationService.deleteFromCollection(locationId, input.getTag(), userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }

}
