package com.epam.ms.service.validation;

import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.service.exception.ConflictingDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserValidator validator;

    @Test
    void should_ThrowException_When_CreateUserWithUsedEmail() {
        User user = createUser();
        Mockito.when(repository.findByEmail(any())).thenReturn(Optional.of(user));
        try {
            validator.validateOnCreate(user);
            fail("Expected ConflictingDataException.");
        } catch (ConflictingDataException expected) {
            assertEquals("The user with the following email already exists: email", expected.getMessage());
        }
    }

    @Test
    void should_ThrowException_When_UpdateUserWithUsedEmail() {
        User user = createUser();
        Mockito.when(repository.findByEmail(any())).thenReturn(Optional.of(user));
        try {
            validator.validateOnUpdate(user, "id1");
            fail("Expected ConflictingDataException.");
        } catch (ConflictingDataException expected) {
            assertEquals("The user with the following email already exists: email", expected.getMessage());
        }
    }

    private User createUser() {
        User user = new User();
        user.setEmail("email");
        user.setId("id");
        return user;
    }
}