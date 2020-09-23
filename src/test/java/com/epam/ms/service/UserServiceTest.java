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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void should_ValidateOnCreate_When_CreateUser() {
        User user = new User();
        service.create(user);
        verify(validator).validateOnCreate(user);
    }

    @Test
    void should_CallRepositorySave_When_CreateUser() {
        User user = new User();
        service.create(user);
        verify(repository).save(user);
    }

    @Test
    void should_SendEmailConfirmation_When_CreateUser() {
        User user = new User();
        service.create(user);
        verify(registrationHandler).sendEmailConfirmation(any());
    }

    @Test
    void should_CallRepositoryFindAll_When_FindAllUsers() {
        service.findAll();
        verify(repository).findAll();
    }

    @Test
    void should_CallRepositoryFindByReceiveNotifications_When_FindUsersByReceiveNotifications() {
        service.findByReceiveNotifications(true);
        verify(repository).findByReceiveNotifications(true);
    }

    @Test
    void should_CallRepositoryFindByReceiveNotifications_When_FindUsersByNotReceiveNotifications() {
        service.findByReceiveNotifications(false);
        verify(repository).findByReceiveNotifications(false);
    }

    @Test
    void should_CallRepositoryFindById_When_FindUserById() {
        String id = "id";
        service.findById(id);
        verify(repository).findById(id);
    }

    @Test
    void should_CallRepositoryTwice_When_UpdateUser() {
        String id = "id";
        User user = new User();
        when(repository.findById(id)).thenReturn(Optional.of(user));
        service.update(id, user);
        verify(repository).findById(id);
        verify(repository).save(user);
    }

    @Test
    void should_ValidateOnUpdate_When_UpdateUser() {
        String id = "id";
        User user = new User();
        when(repository.findById(id)).thenReturn(Optional.of(user));
        service.update(id, user);
        verify(validator).validateOnUpdate(user, id);
    }

    @Test
    void should_SendUserBlockedNotification_When_UserIsBlocked() {
        String id = "id";
        User existingUser = new User();
        existingUser.setActive(true);
        existingUser.setId(id);
        User updatedUser = new User();
        updatedUser.setActive(false);
        when(repository.findById(id)).thenReturn(Optional.of(existingUser));
        service.update(id, updatedUser);
        verify(handler).sendEventToQueue(id, "user.blocked");
    }

    @Test
    void should_SendUserUnblockedNotification_When_UserIsUnblocked() {
        String id = "id";
        User existingUser = new User();
        existingUser.setActive(false);
        existingUser.setId(id);
        User updatedUser = new User();
        updatedUser.setActive(true);
        when(repository.findById(id)).thenReturn(Optional.of(existingUser));
        service.update(id, updatedUser);
        verify(handler).sendEventToQueue(id, "user.unblocked");
    }

    @Test
    void should_UpdateUser_When_EmailIsConfirmed() {
        String id = "id";
        User user = new User();
        when(repository.findById(id)).thenReturn(Optional.of(user));
        service.confirmEmail(id);
        verify(repository).save(user);
    }

    @Test
    void should_CallRepositoryDeleteById_When_DeleteUser() {
        String id = "id";
        service.delete(id);
        verify(repository).deleteById(id);
    }
}