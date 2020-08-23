package com.epam.ms.service.exception;

import java.util.Map;

/**
 * Base Exception for semantics and content exceptions processing
 * @author Dziyana Bahdanava
 */
public class BusinessException extends UserException {
    public BusinessException() {
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String errorCode, Map<String, Object> parameters) {
        this();
        this.setErrorCode(errorCode);
        this.setParameters(parameters);
    }

    public BusinessException(String errorCode, Map<String, Object> parameters, Throwable cause) {
        super(null, cause);
        this.setErrorCode(errorCode);
        this.setParameters(parameters);
    }
}
