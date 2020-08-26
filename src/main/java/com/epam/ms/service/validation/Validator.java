package com.epam.ms.service.validation;

import com.epam.ms.repository.domain.User;

public interface Validator {
    void validateOnCreate(User user);
    void validateOnUpdate(User user, String id);
}
