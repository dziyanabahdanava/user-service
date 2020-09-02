package com.epam.ms.service;

import com.epam.ms.handler.UserRegistrationHandler;
import com.epam.ms.queue.QueueHandler;
import com.epam.ms.repository.UserRepository;
import com.epam.ms.repository.domain.User;
import com.epam.ms.service.validation.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository repository;

    @Mock
    private UserValidator validator;

    @Mock
    private UserRegistrationHandler registrationHandler;

    @Mock
    private QueueHandler handler;

    @InjectMocks
    private UserService service;

    @Test
    void shouldCreateUser() {
        User user = new User();
        service.create(user);
        verify(validator, times(1)).validateOnCreate(user);
        verify(repository, times(1)).save(user);
        verify(registrationHandler, times(1)).sendEmailConfirmation(any());
    }

    @Test
    void shouldFindAllUsers() {
        service.findAll();
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldFindByReceiveNotifications() {
        service.findByReceiveNotifications(true);
        verify(repository, times(1)).findByReceiveNotifications(true);
    }

    @Test
    void shouldFindByNotReceiveNotifications() {
        service.findByReceiveNotifications(false);
        verify(repository, times(1)).findByReceiveNotifications(false);
    }

    @Test
    void shouldFindUserById() {
        String id = "id";
        service.findById(id);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void shouldUpdateUser() {
        String id = "id";
        User user = new User();
        when(repository.findById(id)).thenReturn(Optional.of(user));
        service.update(id, user);
        verify(repository, times(1)).findById(id);
        verify(validator, times(1)).validateOnUpdate(user, id);
    }

    @Test
    void shouldSendUserBlockedNotification() {
        String id = "id";
        User existingUser = new User();
        existingUser.setActive(true);
        existingUser.setId(id);
        User updatedUser = new User();
        updatedUser.setActive(false);
        when(repository.findById(id)).thenReturn(Optional.of(existingUser));
        service.update(id, updatedUser);
        verify(handler, times(1)).sendEventToQueue(id, "user.blocked");
    }

    @Test
    void shouldSendUserUnblockedNotification() {
        String id = "id";
        User existingUser = new User();
        existingUser.setActive(false);
        existingUser.setId(id);
        User updatedUser = new User();
        updatedUser.setActive(true);
        when(repository.findById(id)).thenReturn(Optional.of(existingUser));
        service.update(id, updatedUser);
        verify(handler, times(1)).sendEventToQueue(id, "user.unblocked");
    }

    @Test
    void shouldConfirmEmail() {
        String id = "id";
        User user = new User();
        when(repository.findById(id)).thenReturn(Optional.of(user));
        service.confirmEmail(id);
        verify(repository, times(1)).save(user);
    }

    @Test
    void shouldDeleteUser() {
        String id = "id";
        service.delete(id);
        verify(repository, times(1)).deleteById(id);
    }
}