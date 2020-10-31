package com.epam.ms.service.validation;

public interface Validator<T> {
    void validateOnCreate(T entity);
    void validateOnUpdate(T entity, String id);
}
