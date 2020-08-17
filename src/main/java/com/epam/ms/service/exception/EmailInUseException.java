package com.epam.ms.service.exception;

import java.util.Map;

import static java.util.Objects.nonNull;

public class EmailInUseException extends UserException {

    public EmailInUseException() {
    }

    public EmailInUseException(String message) {
        super(message);
    }

    public EmailInUseException(String errorCode, Map<String, Object> parameters) {
        this();
        this.setErrorCode(errorCode);
        this.setParameters(parameters);
    }

    public EmailInUseException(String errorCode, Map<String, Object> parameters, Throwable cause) {
        super(null, cause);
        this.setErrorCode(errorCode);
        this.setParameters(parameters);
    }

    public void setParameters(Map<String, Object> parameters) {
        if(nonNull(parameters)) {
            parameters.entrySet().stream()
                    .filter(entry -> nonNull(entry.getValue()))
                    .forEach(entry -> this.addUserParameter((String)entry.getKey(), entry.getValue().toString()));
        }
    }
}
