package com.amboucheba.etudeDeCasWeb.Controllers;

import com.amboucheba.etudeDeCasWeb.Models.AuthDetails;
import com.amboucheba.etudeDeCasWeb.Models.Entities.User;
import com.amboucheba.etudeDeCasWeb.Models.Inputs.RegisterInput;
import com.amboucheba.etudeDeCasWeb.Models.Outputs.Users;
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
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created, check location header for uri"),
            @ApiResponse(code = 400, message = "Provided User info not valid, check response body for more details on error")
    })
    public ResponseEntity<Void> addUser(@Valid @RequestBody RegisterInput newUser){

        long newUserId = userService.registerUser(newUser).getId();

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .pathSegment("users", "{id}")
                .buildAndExpand(newUserId)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping( produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned in body"),
            @ApiResponse(code = 401, message = "User unauthenticated: access restricted to authenticated users")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<Users> getUsers(@ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        Users users = new Users(userService.listUsers());
        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(60, TimeUnit.SECONDS)
                        .cachePrivate()
                        .noTransform()
                        .staleIfError(1, TimeUnit.HOURS))
                .body(users);
    }

    @GetMapping(value = "/me", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User returned in body"),
            @ApiResponse(code = 403, message = "Action forbidden: cannot access other users' data"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @ApiImplicitParam(name = "Authorization", required = true, paramType = "header", allowEmptyValue = false, dataTypeClass = String.class, example = "Bearer access_token")
    public ResponseEntity<User> getUserById( @ApiIgnore @AuthenticationPrincipal AuthDetails userDetails){

        return ResponseEntity
                .ok()
                .cacheControl(CacheControl
                        .maxAge(60, TimeUnit.SECONDS)
                        .cachePrivate()
                        .noTransform()
                        .staleIfError(1, TimeUnit.HOURS))
                .body(userService.find( userDetails.getUserId()));
    }

}
