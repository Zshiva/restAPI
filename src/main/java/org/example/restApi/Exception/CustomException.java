package org.example.restApi.Exception;

public class CustomException extends Exception {
    public CustomException(String message, Throwable cause) throws CustomException {
        try {
            throw this;
        } catch (CustomException e) {
            e.initCause(cause);
            throw e;
        }
    }
}


