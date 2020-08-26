package com.epam.ms.repository;

import com.epam.ms.repository.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByReceiveNotifications(Boolean receiveNotifications);
}
