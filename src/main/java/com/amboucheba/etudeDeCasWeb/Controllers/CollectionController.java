package com.amboucheba.etudeDeCasWeb.Controllers;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.Entities.Collection;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.CollectionInput;
import com.amboucheba.etudeDeCasWeb.Models.Outputs.CollectionShare;
import com.amboucheba.etudeDeCasWeb.Models.Outputs.Collections;
import com.amboucheba.etudeDeCasWeb.Services.CollectionService;
import com.amboucheba.etudeDeCasWeb.Util.JwtUtil;
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
public class CollectionController {

    @Autowired
    CollectionService collectionService;


    // Get my collections
    @GetMapping(
            value = "/me/collections",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Collections returned in body"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Collections> getUserCollections(@ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Collections collections = new Collections(collectionService.listCollectionsByUser(userDetails.getUserId()));

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(60, TimeUnit.SECONDS)
                        .cachePrivate()
                        .noTransform()
                        .staleIfError(1, TimeUnit.HOURS))
                .body(collections);
    }


    // Get collections by id --> uri to share with other users
    @GetMapping(
            value = "/collections/{collectionId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Collection returned in body"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 403, message = "User unauthorized: no token provided"),
            @ApiResponse(code = 404, message = "Collection not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Collection> getCollectionById(
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails,
            @PathVariable long collectionId,
            @RequestParam("token") String token
    ){

        Collection collection = collectionService.find(collectionId, token, userDetails.getUserId());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(60, TimeUnit.SECONDS)
                        .cachePrivate()
                        .noTransform()
                        .staleIfError(1, TimeUnit.HOURS))
                .body(collection);
    }

    @PostMapping(
            value = "/collections/{collectionId}/share",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
            })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Collection token returned in body"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 403, message = "User unauthorized: only collection owner can request collection shareable link"),
            @ApiResponse(code = 404, message = "Collection not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<CollectionShare> generateCollectionShareableLink(
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails,
            @PathVariable long collectionId
    ){

        String token = collectionService.generateShareableToken(collectionId, userDetails.getUserId());
        String link = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("collections", "{id}")
                .buildAndExpand(collectionId)
                .toUri()
                .toString();
        CollectionShare collectionShare = new CollectionShare(link, token);

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(60, TimeUnit.SECONDS)
                        .cachePrivate()
                        .noTransform()
                        .staleIfError(1, TimeUnit.HOURS))
                .body(collectionShare);
    }


    @PostMapping(
            value = "/me/collections",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Collection created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Collection info not valid, check response body for more details on error"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "A collection with the same tag name already exists for this user"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> addCollection(
            @Valid @RequestBody CollectionInput newCollection,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){
        long createdCollectionId = collectionService.createCollection(newCollection.getTag(), userDetails.getUserId()).getId();

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("collections", "{id}")
                .buildAndExpand(createdCollectionId)
                .toUri();

        return ResponseEntity.created(location).build();
    }



    @PutMapping(
            value = "/collections/{collectionId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Collection updated and returned in response body"),
            @ApiResponse(code = 400, message = "Provided Collection info not valid, check response body for more details on error"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 404, message = "Collection not found"),
            @ApiResponse(code = 409, message = "A collection with the same tag name already exists for this user"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Collection> updateCollection(
            @PathVariable long collectionId,
            @Valid @RequestBody CollectionInput newCollection,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Collection modifiedCollection = collectionService.updateCollection(
                newCollection, collectionId, userDetails.getUserId()
        );
        return ResponseEntity.ok(modifiedCollection);
    }



    @DeleteMapping(value = "/collections/{collectionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Collection deleted"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users"),
            @ApiResponse(code = 403, message = "Permission denied: Only owner of the collection can delete it"),
            @ApiResponse(code = 404, message = "Collection not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> deleteCollection(
            @PathVariable long collectionId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        collectionService.deleteCollection(collectionId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }


}
