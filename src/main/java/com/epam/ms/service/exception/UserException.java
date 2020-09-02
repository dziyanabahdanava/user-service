package com.epam.ms.service.exception;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

/**
 * Base Exception for user data processing exceptions
 * @author Dziyana Bahdanava
 */
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, String> getUserParameters() {
        return userParameters;
    }

    public void setUserParameters(Map<String, String> userParameters) {
        this.userParameters = userParameters;
    }

    public String getOverrideMessage() {
        return overrideMessage;
    }

    public void setOverrideMessage(String overrideMessage) {
        this.overrideMessage = overrideMessage;
    }

    public String getOverrideHTTPStatusCode() {
        return overrideHTTPStatusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOverrideHTTPStatusCode(String overrideHTTPStatusCode) {
        this.overrideHTTPStatusCode = overrideHTTPStatusCode;
    }

    public void addUserParameter(String key, String value) {
        this.getUserParameters().put(key, value);
    }

    public void setParameters(Map<String, Object> parameters) {
        if(nonNull(parameters)) {
            parameters.entrySet().stream()
                    .filter(entry -> nonNull(entry.getValue()))
                    .forEach(entry -> this.addUserParameter(entry.getKey(), entry.getValue().toString()));
        }
    }
}
