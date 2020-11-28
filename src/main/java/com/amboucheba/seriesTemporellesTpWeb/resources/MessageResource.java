package com.amboucheba.seriesTemporellesTpWeb.resources;

import com.amboucheba.seriesTemporellesTpWeb.exceptions.NotFoundException;
import com.amboucheba.seriesTemporellesTpWeb.models.Message;
import com.amboucheba.seriesTemporellesTpWeb.models.MessageList;
import com.amboucheba.seriesTemporellesTpWeb.repositories.MessageRepository;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/messages")
public class MessageResource {

    @Autowired
    MessageRepository messageRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<MessageList> getAll(@RequestParam(value = "username", defaultValue = "") String username){

        List<Message> messages;

        if (username == null || username.isEmpty()){
            // no username provided -> get all of them
            messages = StreamSupport.stream(messageRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        }
        else{
            // username provided -> get messages of username
            messages = messageRepository.findByUsername(username);
        }

        return ResponseEntity.ok(new MessageList(messages));
    }


    @GetMapping(value = "/{messageId}", produces = "application/json")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Message returned in body"),
        @ApiResponse(code = 404, message = "Message not found")
    })
    public ResponseEntity<Message> getMessageById(@PathVariable("messageId") long messageId){

        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isEmpty()){
            throw new NotFoundException("Message with id " + messageId + " not found");
        }
        return ResponseEntity.ok(message.get());
    }


    @PostMapping(consumes = "application/json" )
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Message created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Message info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addMessage(@Valid @RequestBody Message newMessage){
        // NOT ENOUGH SPACE EXCEPTION
        /// ...
        Message savedMessage = messageRepository.save(newMessage);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMessage.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @PutMapping(value = "/{messageId}", consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message updated and returned in response body"),
            @ApiResponse(code = 201, message = "Message created,  check location header for uri"),
            @ApiResponse(code = 400, message = "Provided Message info not valid, check response body for more details on error")
    })
    public ResponseEntity<Message> updateMessage(@PathVariable("messageId") long messageId, @Valid @RequestBody Message newMessage){
        // NOT ENOUGH SPACE EXCEPTION
        /// ...

        Optional<Message> message = messageRepository.findById(messageId);

        if (message.isPresent()){
            // Message exists so just update it
            Message actualMessage = message.get();
            actualMessage.setText(newMessage.getText());
            actualMessage.setUsername(newMessage.getUsername());
            Message savedMessage = messageRepository.save(actualMessage);
            return ResponseEntity.ok(savedMessage);
        }

        // Message does not exist so create it
        Message savedMessage = messageRepository.save(newMessage);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("messages")
                .path("/{id}")
                .buildAndExpand(savedMessage.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }


    @DeleteMapping(value = "/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Message deleted"),
            @ApiResponse(code = 404, message = "Message not found")
    })
    public ResponseEntity<Void> deleteMessage(@PathVariable("messageId") long messageId){

        if (!messageRepository.existsById(messageId)){
            throw new NotFoundException("Message with id " + messageId + " not found");
        }
        messageRepository.deleteById(messageId);

        return ResponseEntity.noContent().build();
    }
}
