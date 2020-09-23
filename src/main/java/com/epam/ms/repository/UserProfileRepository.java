package com.epam.ms.repository;

import com.epam.ms.repository.domain.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, String> {
    Optional<UserProfile> findByUserId(String userId);

}
