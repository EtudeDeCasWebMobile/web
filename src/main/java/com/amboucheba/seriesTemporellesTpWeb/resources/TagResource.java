package com.amboucheba.seriesTemporellesTpWeb.resources;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Tag;
import com.amboucheba.seriesTemporellesTpWeb.repositories.TagRepository;
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
@RequestMapping("/tags")
public class TagResource {

    @Autowired
    TagRepository tagRepository;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity getAll(){

        List<Tag> tags = StreamSupport.stream(tagRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tags);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE} )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Tag created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Tag info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addTag(@Valid @RequestBody Tag newTag){
        Tag savedTag = tagRepository.save(newTag);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTag.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(value = "/{tagId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag returned in body"),
            @ApiResponse(code = 404, message = "Tag not found")
    })
    public ResponseEntity<Tag> getTagById(@PathVariable("tagId") long tagId){

        Optional<Tag> tag = tagRepository.findById(tagId);
        if (tag.isEmpty()){
            throw new NotFoundException("Tag with id " + tagId + " not found");
        }
        return ResponseEntity.ok(tag.get());
    }

    @PutMapping(
            value = "/{tagId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tag updated and returned in response body"),
            @ApiResponse(code = 201, message = "Tag created,  check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Tag info not valid, check response body for more details on error")
    })
    public ResponseEntity<Tag> updateTag(@PathVariable("tagId") long tagId, @Valid @RequestBody Tag newTag){
        Optional<Tag> tag = tagRepository.findById(tagId);

        if (tag.isPresent()){
            Tag actualTag = tag.get();
            actualTag.setName(newTag.getName());
            Tag savedTag = tagRepository.save(actualTag);
            return ResponseEntity.ok(savedTag);
        }

        // Tag does not exist so create it
        Tag savedEvent = tagRepository.save(newTag);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("tags")
                .path("/{id}")
                .buildAndExpand(savedEvent.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping(value = "/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Tag deleted"),
            @ApiResponse(code = 404, message = "Tag not found")
    })
    public ResponseEntity<Void> deleteTag(@PathVariable("tagId") long tagId){

        if (!tagRepository.existsById(tagId)){
            throw new NotFoundException("Tag with id " + tagId + " not found");
        }
        tagRepository.deleteById(tagId);

        return ResponseEntity.noContent().build();
    }
}
