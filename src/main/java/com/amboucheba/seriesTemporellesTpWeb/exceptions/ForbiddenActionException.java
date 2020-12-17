package com.amboucheba.seriesTemporellesTpWeb.exceptions;

public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String message) {
        super(message);
    }

    public ForbiddenActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
