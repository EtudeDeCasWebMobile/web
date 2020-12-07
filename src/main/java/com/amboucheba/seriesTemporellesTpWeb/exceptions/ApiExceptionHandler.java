package com.amboucheba.seriesTemporellesTpWeb.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(value = {DuplicateResourceException.class})
    public ResponseEntity<ApiException> handleDuplicateResourceException(DuplicateResourceException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.CONFLICT);
        return new ResponseEntity<>(apiException, HttpStatus.CONFLICT );
    }

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

    // Unused: Replaced with MethodArgumentNotValidException
    @ExceptionHandler(value = {PayloadTooLargeException.class})
    public ResponseEntity<ApiException> handlePayloadTooLargeException(PayloadTooLargeException e){
        ApiException apiException = new ApiException(e.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE);
        return new ResponseEntity<>(apiException, HttpStatus.PAYLOAD_TOO_LARGE );
    }
}
