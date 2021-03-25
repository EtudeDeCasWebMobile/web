package com.amboucheba.etudeDeCasWeb.Exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(value = {RestException.class})
    public ResponseEntity<ApiException> handleRestException(RestException e){
        ApiException apiException = new ApiException(e.getMessage(), e.getStatus());
        return new ResponseEntity<>(apiException, e.getStatus() );
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiException> handleNotFoundException(NotFoundException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<ApiException> handleExpiredJwtException(ExpiredJwtException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED );
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    public ResponseEntity<ApiException> handleMalformedJwtException(MalformedJwtException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED );
    }

    @ExceptionHandler(value = {SignatureException.class})
    public ResponseEntity<ApiException> handleSignatureException(SignatureException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED );
    }

    @ExceptionHandler(value = {DuplicateResourceException.class})
    public ResponseEntity<ApiException> handleDuplicateResourceException(DuplicateResourceException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT );
    }

    @ExceptionHandler(value = {ForbiddenActionException.class})
    public ResponseEntity<ApiException> handleForbiddenActionException(ForbiddenActionException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN );
    }

//    @ExceptionHandler(value = {AccessDeniedException.class})
//    public ResponseEntity<ApiException> handleAccessDeniedException(AccessDeniedException e){
//        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED);
//        return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED );
//    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiException> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        ApiException apiException = new ApiException(message, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<ApiException> handleMissingServletRequestParameterException(HttpMessageNotReadableException e){
        ApiException apiException = new ApiException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST );
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<ApiException> handleBadCredentialsException(BadCredentialsException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST );
    }


    // Unused: Replaced with MethodArgumentNotValidException
    @ExceptionHandler(value = {PayloadTooLargeException.class})
    public ResponseEntity<ApiException> handlePayloadTooLargeException(PayloadTooLargeException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
        return new ResponseEntity<>(apiException, HttpStatus.PAYLOAD_TOO_LARGE );
    }
}
