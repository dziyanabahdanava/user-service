package com.epam.ms.service.exception;

import java.util.Map;

/**
 * Base Exception to process conflict with the current state of the target resource
 * @author Dziyana Bahdanava
 */
public class ConflictingDataException extends UserException {

    public ConflictingDataException() {
    }

    public ConflictingDataException(String message) {
        super(message);
    }

    public ConflictingDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConflictingDataException(String errorCode, Map<String, Object> parameters) {
        this();
        this.setErrorCode(errorCode);
        this.setParameters(parameters);
    }

    public ConflictingDataException(String errorCode, Map<String, Object> parameters, Throwable cause) {
        super(null, cause);
        this.setErrorCode(errorCode);
        this.setParameters(parameters);
    }
}
