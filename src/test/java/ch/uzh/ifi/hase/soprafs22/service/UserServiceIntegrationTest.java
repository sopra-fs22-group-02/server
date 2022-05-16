package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;


import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

   User testUser = new User();
   testUser.setEmail("firstname.lastname@uzh.ch");
   testUser.setUsername("testUsername");
   testUser.setPassword("password");

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getUserId(), createdUser.getUserId());
    assertEquals(testUser.getEmail(), createdUser.getEmail());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }


  @Test
  public void createUser_duplicateEmail_And_duplicateUsername_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setEmail("xyz@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      User createdUser = userService.createUser(testUser);

      // attempt to create second user with same username and email
      User testUser2 = new User();

      testUser2.setEmail("xyz@uzh.ch");
      testUser2.setUsername("testUsername");
      testUser.setPassword("password");

      // check that an error is thrown
      assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setEmail("xyz@uzh.ch");
    testUser.setUsername("testUsername");
    testUser.setPassword("password");
    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the name but forget about the username
    testUser2.setEmail("helloWorld@uzh.ch");
    testUser2.setUsername("testUsername");
    testUser.setPassword("password");

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

  @Test
  public void createUser_duplicateEmail_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      User testUser = new User();
      testUser.setEmail("xyz@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      User createdUser = userService.createUser(testUser);

      // attempt to create second user with same username
      User testUser2 = new User();

      testUser2.setEmail("xyz@uzh.ch");
      testUser2.setUsername("anotherUsername");
      testUser.setPassword("password");

      // check that an error is thrown
      assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

  @Test
  public void UserLogin_UserNotRegistered_throwsException() {
      assertNull(userRepository.findByUsername("testUsername"));

      assertThrows(ResponseStatusException.class, () -> userService.login(null, null));
  }

  @Test
  public void UserLogin_PasswordIncorrect_throwsException() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      userService.createUser(testUser);

      User anotherTestUser = new User();
      anotherTestUser.setEmail("abc@uzh.ch");
      anotherTestUser.setUsername("anotherUsername");
      anotherTestUser.setPassword("4321");
      userService.createUser(anotherTestUser);

      // then
      assertThrows(ResponseStatusException.class, () -> userService.login(testUser.getUsername(), anotherTestUser));
  }

  @Test
  public void UserLogin_UsernameIncorrect_throwsException() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      userService.createUser(testUser);

      User anotherTestUser = new User();
      anotherTestUser.setEmail("abc@uzh.ch");
      anotherTestUser.setUsername("anotherUsername");
      anotherTestUser.setPassword("password");
      userService.createUser(anotherTestUser);

      // then
      assertThrows(ResponseStatusException.class, () -> userService.login(testUser.getUsername(), anotherTestUser));
  }

  @Test
  public void UserLogin_checkStatusChange() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setStatus(UserStatus.OFFLINE);
      userService.createUser(testUser);

      // when
      User loggedInUser = userService.login(testUser.getUsername(), testUser);

      // then
      assertEquals(UserStatus.ONLINE, loggedInUser.getStatus());
  }

  @Test
  public void UserLogout_checkStatusChange() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setStatus(UserStatus.ONLINE);
      userService.createUser(testUser);

      // when
      User loggedOutUser = userService.logout(testUser.getUsername());

      // then
      assertEquals(UserStatus.OFFLINE, loggedOutUser.getStatus());
  }

  @Test
  public void getUsers_UsersFound_UsersReturned() {
      assertNull(userRepository.findByUsername("testUsername"));

      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setStatus(UserStatus.ONLINE);
      User newUser = userService.createUser(testUser);
      User foundUser = userService.getUsers().get(0);

      User testUser2 = new User();
      testUser2.setEmail("xyz@uzh.ch");
      testUser2.setUsername("anotherUsername");
      testUser2.setPassword("1234");
      testUser2.setStatus(UserStatus.ONLINE);
      User anotherNewUser = userService.createUser(testUser2);
      User anotherFoundUser = userService.getUsers().get(1);

      // then
      assertEquals(newUser.getUserId(), foundUser.getUserId());
      assertEquals(anotherNewUser.getUserId(), anotherFoundUser.getUserId());
  }

  @Test
  public void getUser_IdFound_UserReturned() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setStatus(UserStatus.ONLINE);
      User createdUser = userService.createUser(testUser);

      // then
      assertEquals(testUser.getUserId(), userService.findUserById(createdUser.getUserId()).getUserId());
  }

  // test if 404 (NOT_FOUND) is thrown when trying to get a non-existing user
  @Test
  public void getUser_IdNotFound_throwsException() {
      assertNull(userRepository.findByUserId(1));

      assertThrows(ResponseStatusException.class, () -> userService.findUserById(1));
  }

  // test if 404 (NOT_FOUND) is thrown when trying to update a non-existing user
  @Test
  public void updateUser_IdNotFound_throwsException() {
      assertNull(userRepository.findByUserId(1));

      assertThrows(ResponseStatusException.class, () -> userService.updateUser(null, 1));
  }

  // test if 403 (FORBIDDEN) is thrown when trying to update username to NULL
  @Test
  public void updateUser_UpdateUsernameToNull_throwsException() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setBio("hello my name is Peter.");
      testUser.setProfilePicture("some link");
      User createdUser = userService.createUser(testUser);

      createdUser.setUsername(null);
      createdUser.setPassword("1234");
      createdUser.setBio("I am from Switzerland.");
      createdUser.setProfilePicture("some other link");

      assertThrows(ResponseStatusException.class, () -> userService.updateUser(createdUser, 1));
  }

  // test if 403 (FORBIDDEN) is thrown when trying to update password to NULL
  @Test
  public void updateUser_UpdatePasswordToNull_throwsException() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setBio("hello my name is Peter.");
      testUser.setProfilePicture("some link");
      User createdUser = userService.createUser(testUser);

      createdUser.setUsername("someUsername");
      createdUser.setPassword(null);
      createdUser.setBio("I am from Switzerland.");
      createdUser.setProfilePicture("some other link");

      assertThrows(ResponseStatusException.class, () -> userService.updateUser(createdUser, 1));
  }

  @Test
  public void updateUser_validInputs_success() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setBio("hello my name is Peter.");
      testUser.setProfilePicture("some link");
      User createdUser = userService.createUser(testUser);

      createdUser.setUsername("anotherUsername");
      createdUser.setPassword("1234");
      createdUser.setBio("I am from Switzerland.");
      createdUser.setProfilePicture("some other link");

      userService.updateUser(createdUser, testUser.getUserId());

      // then
      assertEquals(createdUser, testUser);
  }

  @Test
  public void updateUser_checkIfTokenIsEqual_SameUser() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setBio("hello my name is Peter.");
      testUser.setProfilePicture("some link");
      User createdUser = userService.createUser(testUser);

      createdUser.setUsername("testUsername");
      createdUser.setPassword("1234");
      createdUser.setBio("I am from Switzerland.");
      createdUser.setProfilePicture("some other link");

      userService.updateUser(createdUser, testUser.getUserId());

      assertEquals(testUser.getToken(), createdUser.getToken());
  }

  @Test
  public void updateUser_checkIfTokenIsEqual_NotSameUser() {
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      testUser.setBio("hello my name is Peter.");
      testUser.setProfilePicture("some link");
      User createdUser = userService.createUser(testUser);

      User anotherTestUser = new User();
      anotherTestUser.setEmail("hello@uzh.ch");
      anotherTestUser.setUsername("anotherUsername");
      anotherTestUser.setPassword("1234");
      anotherTestUser.setBio("I am from Switzerland.");
      anotherTestUser.setProfilePicture("some other link");
      User anotherCreatedUser = userService.createUser(anotherTestUser);

      testUser.setUsername("anotherUsername");

      assertThrows(ResponseStatusException.class, () -> userService.updateUser(anotherTestUser, testUser.getUserId()));
  }
}
