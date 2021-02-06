package com.amboucheba.etudeDeCasWeb.Controllers;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists.TagList;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Tag;
import com.amboucheba.etudeDeCasWeb.Services.TagService;
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
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping(
            value = "/events/{eventId}/tags",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success, check response body"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Event not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<TagList> getTagsByEvent(
            @PathVariable long eventId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        List<Tag> tags = tagService.listTagsByEvent(eventId, userDetails.getUserId());

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                    .maxAge(10, TimeUnit.SECONDS)
                    .cachePublic()
                    .proxyRevalidate()
                    .staleIfError(5, TimeUnit.SECONDS))
                .body(new TagList(tags));
    }

    @PostMapping(
            value = "/events/{eventId}/tags",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tag created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Tag info not valid, check response body for more details on error"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Event not found"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> addTagToEvent(
            @Valid @RequestBody Tag newTag,
            @PathVariable long eventId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Tag savedTag = tagService.addTagToEvent(eventId, newTag, userDetails.getUserId());

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("tags", "{id}")
                .buildAndExpand(savedTag.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(
            value = "/tags/{tagId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag returned in body"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Tag not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Tag> getTagById(
            @PathVariable long tagId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Tag tag = tagService.find(tagId, userDetails.getUserId());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                    .maxAge(10, TimeUnit.SECONDS)
                    .cachePublic()
                    .proxyRevalidate()
                    .staleIfError(5, TimeUnit.SECONDS))
                .body(tag);
    }

    @PutMapping(
            value = "/tags/{tagId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag updated and returned in response body"),
            @ApiResponse(code = 400, message = "Provided Tag info not valid, check response body for more details on error"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Tag not found"),
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Tag> updateTag(
            @PathVariable long tagId,
            @Valid @RequestBody Tag newTag,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Tag tag = tagService.updateTag(tagId, newTag, userDetails.getUserId());
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping(value = "/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Tag deleted"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "Tag not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Void> deleteTag(
            @PathVariable long tagId,
            @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        tagService.remove(tagId, userDetails.getUserId());
        return ResponseEntity.noContent().build();
    }
}
