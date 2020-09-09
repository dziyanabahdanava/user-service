package com.epam.ms.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Base Exception for user data processing exceptions
 * @author Dziyana Bahdanava
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserException extends RuntimeException {
    private String errorCode;
    private Map<String, String> userParameters = new HashMap<>();
    private String overrideMessage;
    private String message;
    private String overrideHTTPStatusCode;

    public UserException() {
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public UserException(String message) {
        this.setMessage(message);
    }

    private void addUserParameter(String key, String value) {
        this.getUserParameters().put(key, value);
    }

    private void setParameters(Map<String, Object> parameters) {
        if(nonNull(parameters)) {
            parameters.entrySet().stream()
                    .filter(entry -> nonNull(entry.getValue()))
                    .forEach(entry -> this.addUserParameter(entry.getKey(), entry.getValue().toString()));
        }
    }
}
