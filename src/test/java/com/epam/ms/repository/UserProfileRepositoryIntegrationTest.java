package com.epam.ms.repository;

import com.epam.ms.repository.domain.Goal;
import com.epam.ms.repository.domain.PhysicalActivity;
import com.epam.ms.repository.domain.User;
import com.epam.ms.repository.domain.UserProfile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserProfileRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserProfileRepository repository;

    @Test
    public void should_ReturnUserProfile_When_FindById() {
        // given
        UserProfile profile = createDefaultProfile();
        UserProfile persisted = entityManager.persist(profile);
        entityManager.flush();

        // when
        Optional<UserProfile> found = repository.findById(persisted.getId());

        // then
        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getId()).isEqualTo(persisted.getId());
    }

    @Test
    public void should_ReturnUsersProfiles_When_FindAll() {
        // given
        UserProfile profile = createDefaultProfile();
        entityManager.persist(profile);
        entityManager.flush();

        // when
        List<UserProfile> found = (List<UserProfile>) repository.findAll();

        // then
        assertThat(found.get(0).getAge()).isEqualTo(25);
    }

    private UserProfile createDefaultProfile() {
        UserProfile profile = new UserProfile();
        User user = new User();
        user.setId("id");
        profile.setPhysicalActivity(PhysicalActivity.LOW_ACTIVITY);
        profile.setGoal(Goal.BODY_RELIEF);
        profile.setWeight(80);
        profile.setHeight(175);
        profile.setAge(25);
        profile.setUser(user);
        return profile;
    }
}