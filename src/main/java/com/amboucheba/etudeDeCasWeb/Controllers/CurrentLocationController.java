package com.amboucheba.etudeDeCasWeb.Controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.LocationShare;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Location;
import com.amboucheba.etudeDeCasWeb.Services.LocationService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/user")
public class CurrentLocationController {
	 @Autowired 
	    LocationService locationService;
	 
	 @RequestMapping(value = "/custom", method = RequestMethod.POST)
	    public String custom() {
	        return "custom";
	    }
	 


	 @PostMapping(
	            value = "/{userId}/currentLocation/share",
	            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
	            })
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Collection token returned in body"),
	            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
	            @ApiResponse(code = 403, message = "User unauthorized: only collection owner can request collection shareable link"),
	            @ApiResponse(code = 404, message = "Collection not found")
	    })
	 @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
	 	    public ResponseEntity<LocationShare> generateCollectionShareableLink(
	 	    		@ApiIgnore @AuthenticationPrincipal AuthDetails userDetails,
	            @PathVariable("userId") long userId
	            
	    ){

	        String token = locationService.generateShareableToken(userId);
	        String link = ServletUriComponentsBuilder.fromCurrentContextPath()
	                .pathSegment("currentLocation", "{id}")
	                .buildAndExpand(userId)
	                .toUri()
	                .toString();
	        LocationShare locationShare = new LocationShare(link, token);

	        return ResponseEntity
	                .ok()
	                .cacheControl(CacheControl
	                        .maxAge(60, TimeUnit.SECONDS)
	                        .cachePrivate()
	                        .noTransform()
	                        .staleIfError(1, TimeUnit.HOURS))
	                .body(locationShare);
	    }
	 
	 @GetMapping(
	            value = "/currentLocation/{userId}",
	            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
	    })
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Current location returned in body"),
	            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
	            @ApiResponse(code = 403, message = "User unauthorized: no token provided"),
	            @ApiResponse(code = 404, message = "Current location not found")
	    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
	    public ResponseEntity<Location> getCollectionById(
	            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails,
	            @PathVariable long userId,
	            @RequestParam("token") String token
	    ){

	        Location currentLocation = locationService.find(userId, token);

	        return ResponseEntity
	                .ok()
	                .cacheControl(CacheControl
	                        .maxAge(60, TimeUnit.SECONDS)
	                        .cachePrivate()
	                        .noTransform()
	                        .staleIfError(1, TimeUnit.HOURS))
	                .body(currentLocation);
	    }
	 
	 @PutMapping(
	            value = "/{userId}/currentLocation",
	            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
	            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
	    )
	    @ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Current location updated and returned in response body"),
	            @ApiResponse(code = 400, message = "Provided Current location info not valid, check response body for more details on error"),
	            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
	            
	    })
	 @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
	  public ResponseEntity<Location> updateLocation(
	            @PathVariable("userId") long userId,
	            @Valid @RequestBody Location newLocation,
	            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

	        Location updatedLocation = locationService.updateCurrentLocation(userId, newLocation);
	        return ResponseEntity.ok(updatedLocation);
	    }

}
