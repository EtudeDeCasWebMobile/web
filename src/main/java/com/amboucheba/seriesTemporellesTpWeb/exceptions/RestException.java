package com.amboucheba.seriesTemporellesTpWeb.exceptions;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class RestException extends Throwable{
    @Override
    public String getMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    private String errMessage;
    private HttpStatus status;


    public RestException(String message, HttpStatus status) {
        this.errMessage = message;
        this.status = status;
    }

    public Map<String, Object> serialized(){
        Map<String, Object> m= new HashMap();
        m.put("error message", this.errMessage);
        m.put("status", this.status.toString());
        return m;
    }
}
