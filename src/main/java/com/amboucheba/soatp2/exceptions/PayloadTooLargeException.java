package com.amboucheba.soatp2.exceptions;

public class PayloadTooLargeException  extends RuntimeException{


    public PayloadTooLargeException(String message) {
        super(message);
    }

    public PayloadTooLargeException(String message, Throwable cause) {
        super(message, cause);
    }
}
