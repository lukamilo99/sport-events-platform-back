package sport.app.sport_connecting_people.unit.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import sport.app.sport_connecting_people.entity.enums.AuthenticationProvider;
import sport.app.sport_connecting_people.entity.User;
import sport.app.sport_connecting_people.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUpdateUserEnabledStatus() {
        User user = getEnabledUser();

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        int updatedRows = userRepository.updateUserEnabledStatus(user.getId(), false);

        assertEquals(1, updatedRows);
        User updatedUser = entityManager.find(User.class, user.getId());
        assertFalse(updatedUser.isEnabled());
    }

    private User getEnabledUser() {
        User user = new User();
        user.setFirstname("Test");
        user.setLastname("Test");
        user.setEmail("test@gmail.com");
        user.setPassword("encodedPassword");
        user.setEnabled(true);
        user.setProvider(AuthenticationProvider.local);
        user.setProviderId("providerId");
        return user;
    }
}
