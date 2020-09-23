package com.epam.ms.service.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;


@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {
    private String errorCode;
    private Map<String, String> userParameters = new HashMap<>();
    private String overrideMessage;
    private String overrideHTTPStatusCode;

    public ServiceException() {
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String errorCode, Map<String, Object> parameters) {
        this();
        this.errorCode = errorCode;
        this.setParameters(parameters);
    }

    public ServiceException(String errorCode, Map<String, Object> parameters, Throwable cause) {
        super(null, cause);
        this.errorCode = errorCode;
        this.setParameters(parameters);
    }

    private void setParameters(Map<String, Object> parameters) {
        if(nonNull(parameters)) {
            parameters.entrySet().stream()
                    .filter(entry -> nonNull(entry.getValue()))
                    .forEach(entry -> this.addUserParameter(entry.getKey(), entry.getValue().toString()));
        }
    }

    private void addUserParameter(String key, String value) {
        this.getUserParameters().put(key, value);
    }
}
