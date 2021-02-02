package com.amboucheba.etudeDeCasWeb.Exceptions;

public class PayloadTooLargeException  extends RuntimeException{


    public PayloadTooLargeException(String message) {
        super(message);
    }

    public PayloadTooLargeException(String message, Throwable cause) {
        super(message, cause);
    }
}
