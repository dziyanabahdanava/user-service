package com.epam.ms.repository;

import com.epam.ms.repository.domain.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, String> {
}
