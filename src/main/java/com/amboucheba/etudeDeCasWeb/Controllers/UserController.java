package com.amboucheba.etudeDeCasWeb.Controllers;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.RegisterUserInput;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.Users;
import com.amboucheba.etudeDeCasWeb.Models.ToDelete.ModelLists.UserList;
import com.amboucheba.etudeDeCasWeb.Services.UserService;
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
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    // Should be removed
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<UserList> getAll(){

        List<Users> users = userService.listUsers();

        return ResponseEntity.ok(new UserList(users));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided User info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addUser(@Valid @RequestBody RegisterUserInput newUser){

        long newUserId = userService.registerUser(newUser).getId();

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("users", "{id}")
                .buildAndExpand(newUserId)
                .toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned in body"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Users> getUserById(@PathVariable("userId") long userId, @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(60, TimeUnit.SECONDS)
                        .cachePrivate()
                        .noTransform()
                        .staleIfError(1, TimeUnit.HOURS))
                .body(userService.find(userId, userDetails.getUserId()));
    }

//    @PutMapping(value = "/{userId}", consumes = "application/json", produces = "application/json")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "User updated and returned in response body"),
//            @ApiResponse(code = 201, message = "User created,  check location header for uri"),
//            @ApiResponse(code = 400, message = "Provided User info not valid, check response body for more details on error")
//    })
//    public ResponseEntity<User> updateUser(@PathVariable("userId") long userId, @Valid @RequestBody User newUser){
//        Optional<User> user = userRepository.findById(userId);
//
//        if (user.isPresent()){
//            User actualUser = user.get();
//            actualUser.setUsername(newUser.getUsername());
//            actualUser.setPassword(newUser.getPassword());
//            User savedUser = userRepository.save(actualUser);
//            return ResponseEntity.ok(savedUser);
//        }
//
//        // User does not exist so create it
//        User savedUser = userRepository.save(newUser);
//
//        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path("users")
//                .path("/{id}")
//                .buildAndExpand(savedUser.getId())
//                .toUri();
//
//        return ResponseEntity.created(location).build();
//    }


//    @DeleteMapping(value = "/{userId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @ApiResponses(value = {
//            @ApiResponse(code = 204, message = "User deleted"),
//            @ApiResponse(code = 404, message = "User not found")
//    })
//    public ResponseEntity<Void> deleteUser(@PathVariable("userId") long userId){
//
//        if (!userRepository.existsById(userId)){
//            throw new NotFoundException("User with id " + userId + " not found");
//        }
//        userRepository.deleteById(userId);
//
//        return ResponseEntity.noContent().build();
//    }
}
