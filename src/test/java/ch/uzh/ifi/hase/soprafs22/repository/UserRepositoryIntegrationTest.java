package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findById_success() {
        // given
        User user = new User();
        user.setEmail("firstname.lastname@uzh.ch");
        user.setUsername("username");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setBio("Hi there, I'm a student at UZH!");
        user.setProfilePicture("some link");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUserId(user.getUserId());

        // then
        assertEquals(found.getUserId(), user.getUserId());
        assertEquals(found.getEmail(), user.getEmail());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getFirstName(), user.getFirstName());
        assertEquals(found.getLastName(), user.getLastName());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getStatus(), user.getStatus());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getBio(), user.getBio());
        assertEquals(found.getProfilePicture(), user.getProfilePicture());
    }

    @Test
    public void findByUsername_success() {
        // given
        User user = new User();
        user.setEmail("firstname.lastname@uzh.ch");
        user.setUsername("username");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setBio("Hi there, I'm a student at UZH!");
        user.setProfilePicture("some link");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(user.getUsername());

        // then
        assertEquals(found.getUserId(), user.getUserId());
        assertEquals(found.getEmail(), user.getEmail());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getFirstName(), user.getFirstName());
        assertEquals(found.getLastName(), user.getLastName());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getStatus(), user.getStatus());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getBio(), user.getBio());
        assertEquals(found.getProfilePicture(), user.getProfilePicture());
    }

    @Test
    public void findByEmail_success() {
        // given
        User user = new User();
        user.setEmail("firstname.lastname@uzh.ch");
        user.setUsername("username");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setBio("Hi there, I'm a student at UZH!");
        user.setProfilePicture("some link");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByEmail(user.getEmail());

        // then
        assertEquals(found.getUserId(), user.getUserId());
        assertEquals(found.getEmail(), user.getEmail());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getFirstName(), user.getFirstName());
        assertEquals(found.getLastName(), user.getLastName());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getStatus(), user.getStatus());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getBio(), user.getBio());
        assertEquals(found.getProfilePicture(), user.getProfilePicture());
    }
}
