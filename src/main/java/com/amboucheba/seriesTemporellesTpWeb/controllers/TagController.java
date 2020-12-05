package com.amboucheba.seriesTemporellesTpWeb.controllers;

import com.amboucheba.seriesTemporellesTpWeb.models.ModelLists.TagList;
import com.amboucheba.seriesTemporellesTpWeb.models.Tag;
import com.amboucheba.seriesTemporellesTpWeb.services.TagService;
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

@RestController
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping(
            value = "/events/{eventId}/tags",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success, check response body"),
            @ApiResponse(code = 404, message = "Event not found"),})
    public ResponseEntity<TagList> getTagsByEvent(@PathVariable long eventId){

        List<Tag> tags = tagService.listTagsByEvent(eventId);

        return ResponseEntity.ok(new TagList(tags));
    }

    @PostMapping(
            value = "/events/{eventId}/tags",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tag created, check location header for uri"),
            @ApiResponse(code = 404, message = "Event not found"),
            @ApiResponse(code = 400, message = "Provided Tag info not valid, check response body for more details on error") })
    public ResponseEntity<Void> addTagToEvent(@Valid @RequestBody Tag newTag, @PathVariable long eventId){

        Tag savedTag = tagService.addTagToEvent(eventId, newTag);

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
            @ApiResponse(code = 404, message = "Tag not found") })
    public ResponseEntity<Tag> getTagById(@PathVariable long tagId){

        Tag tag = tagService.find(tagId);
        return ResponseEntity.ok(tag);
    }

    @PutMapping(
            value = "/tags/{tagId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag updated and returned in response body"),
            @ApiResponse(code = 404, message = "Tag not found"),
            @ApiResponse(code = 400, message = "Provided Tag info not valid, check response body for more details on error") })
    public ResponseEntity<Tag> updateTag(@PathVariable long tagId, @Valid @RequestBody Tag newTag){

        Tag tag = tagService.updateTag(tagId, newTag );
        return ResponseEntity.ok(tag);
    }

    @DeleteMapping(value = "/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Tag deleted"),
            @ApiResponse(code = 404, message = "Tag not found") })
    public ResponseEntity<Void> deleteTag(@PathVariable long tagId){

        tagService.remove(tagId);
        return ResponseEntity.noContent().build();
    }
}
