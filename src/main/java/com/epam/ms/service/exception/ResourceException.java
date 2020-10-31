package com.epam.ms.service.exception;

import java.util.Map;

/**
 * Base Exception to to indicate that the server could not find requested resource
 * @author Dziyana Bahdanava
 */
public class ResourceException extends UserException {
    public ResourceException() {
    }

    public ResourceException(String message) {
        super(message);
    }

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(String errorCode, Map<String, Object> parameters) {
        this();
        this.setErrorCode(errorCode);
        this.setParameters(parameters);
    }

    public ResourceException(String errorCode, Map<String, Object> parameters, Throwable cause) {
        super(null, cause);
        this.setErrorCode(errorCode);
        this.setParameters(parameters);
    }
}
