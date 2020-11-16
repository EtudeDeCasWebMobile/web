package com.amboucheba.soatp2.resources;

import com.amboucheba.soatp2.exceptions.NotFoundException;
import com.amboucheba.soatp2.models.Message;
import com.amboucheba.soatp2.models.MessageList;
import com.amboucheba.soatp2.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping()
    public ResponseEntity<MessageList> getAll(){

        List<Message> messages = StreamSupport.stream(messageRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new MessageList(messages));
    }

    @GetMapping(value = "/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable("messageId") long messageId){

        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isEmpty()){
            throw new NotFoundException("Message with id " + messageId + " not found");
        }
        return ResponseEntity.ok(message.get());
    }

    @PostMapping
    public ResponseEntity<Object> addMessage(@Valid @RequestBody Message newMessage){
        // NOT ENOUGH SPACE EXCEPTION
        /// ...
        Message savedMessage = messageRepository.save(newMessage);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedMessage.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{messageId}")
    public ResponseEntity<Object> updateMessage(@PathVariable("messageId") long messageId, @Valid @RequestBody Message newMessage){
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
    public ResponseEntity<Object> deleteMessage(@PathVariable("messageId") long messageId){

        if (!messageRepository.existsById(messageId)){
            throw new NotFoundException("Message with id " + messageId + " not found");
        }
        messageRepository.deleteById(messageId);

        String responseBody = "Message with id " + messageId + " has been deleted.";
        return ResponseEntity.ok(responseBody);
    }
}
