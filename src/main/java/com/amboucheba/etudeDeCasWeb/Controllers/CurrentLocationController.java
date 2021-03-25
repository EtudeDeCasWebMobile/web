package com.amboucheba.etudeDeCasWeb.Controllers;

import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import com.amboucheba.etudeDeCasWeb.Models.Entities.Position;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.PositionInput;
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
@RequestMapping("/users")
public class CurrentLocationController {
	 @Autowired 
	    LocationService locationService;
	 
//	 @RequestMapping(value = "/custom", method = RequestMethod.POST)
//	    public String custom() {
//	        return "custom";
//	    }


	@PostMapping(
	            value = "/{userId}/position/share",
	            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User's position uri and token returned in body"),
			@ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
			@ApiResponse(code = 403, message = "User unauthorized: only concerned user can request position shareable link"),
			@ApiResponse(code = 404, message = "User's position not set")
	})
	@ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
		public ResponseEntity<LocationShare> generatePositionShareableLink(
			@ApiIgnore @AuthenticationPrincipal AuthDetails userDetails,
			@PathVariable("userId") long userId

	){

		long initiatorId = userDetails.getUserId();
		String token = locationService.generateShareableToken(userId, initiatorId);
		String link = ServletUriComponentsBuilder.fromCurrentContextPath()
				.pathSegment("users", "{id}", "position")
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
			value = "/{userId}/position",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
	})
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "User's Position returned in body"),
			@ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
			@ApiResponse(code = 403, message = "User unauthorized: no token provided"),
			@ApiResponse(code = 404, message = "User's position not set")
	})
	@ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
	public ResponseEntity<Position> getUserCurrentPositionByUserId(
			@ApiIgnore @AuthenticationPrincipal AuthDetails userDetails,
			@PathVariable long userId,
			@RequestParam("token") String token
	){

		Position currentLocation = locationService.find(userId, token, userDetails.getUserId());

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
	            value = "/{userId}/position",
	            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
	            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Current user position updated and returned in response body"),
			@ApiResponse(code = 400, message = "Provided position info not valid, check response body for more details on error"),
			@ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
			@ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
			@ApiResponse(code = 404, message = "User not found")
	})
 	@ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
	public ResponseEntity<Position> updateLocation(
			@PathVariable("userId") long userId,
			@Valid @RequestBody PositionInput newLocation,
			@ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

		long initiatorId = userDetails.getUserId();
		Position updatedLocation = locationService.updateCurrentLocation(userId, newLocation, initiatorId);
		return ResponseEntity.ok(updatedLocation);
	}

}
