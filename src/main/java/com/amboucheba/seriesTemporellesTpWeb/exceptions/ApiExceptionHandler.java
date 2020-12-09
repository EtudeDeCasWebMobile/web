package com.amboucheba.seriesTemporellesTpWeb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND );
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
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
    public ResponseEntity<Object> handlePayloadTooLargeException(PayloadTooLargeException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
        return new ResponseEntity<>(apiException, HttpStatus.PAYLOAD_TOO_LARGE );
    }
}
