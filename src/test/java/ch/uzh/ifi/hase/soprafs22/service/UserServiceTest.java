package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setEmail("xyz@uzh.ch");
    testUser.setUsername("testUsername");
    testUser.setPassword("1234");

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getUserId(), createdUser.getUserId());
    assertEquals(testUser.getEmail(), createdUser.getEmail());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateEmail_And_duplicateUsername_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }
  @Test
  public void createUser_duplicateEmail_throwsException() {
      // given -> a first user has already been created
      userService.createUser(testUser);

      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }
  @Test
  public void createUser_duplicateUsername_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void UserLogin_UserNotRegistered_throwsException() {
      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

      // then
      assertThrows(ResponseStatusException.class, () -> userService.login(null, null));
  }

  @Test
  public void UserLogin_PasswordIncorrect_throwsException() {
      // given
      User anotherTestUser = new User();
      anotherTestUser.setEmail("abc@uzh.ch");
      anotherTestUser.setUsername("anotherUsername");
      anotherTestUser.setPassword("4321");
      userService.createUser(anotherTestUser);

      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser);

      // then
      assertThrows(ResponseStatusException.class, () -> userService.login(testUser.getUsername(), anotherTestUser));
  }

  @Test
  public void UserLogin_UsernameIncorrect_throwsException() {
      // given
      User anotherTestUser = new User();
      anotherTestUser.setEmail("abc@uzh.ch");
      anotherTestUser.setUsername("anotherUsername");
      anotherTestUser.setPassword("1234");
      userService.createUser(anotherTestUser);

      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser);

      // then
      assertThrows(ResponseStatusException.class, () -> userService.login(testUser.getUsername(), anotherTestUser));
  }


  @Test
  public void UserLogin_checkStatusChange() {
      // given
      testUser.setStatus(UserStatus.OFFLINE);

      // when
      Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser);
      userService.login(testUser.getUsername(), testUser);

      // then
      assertEquals(UserStatus.ONLINE, testUser.getStatus());
  }

  @Test
  public void UserLogout_checkStatusChange() {
      // given
      testUser.setStatus(UserStatus.ONLINE);

      // when
      Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(testUser);
      Mockito.when(userService.logout(Mockito.anyString())).thenReturn(testUser);

      // then
      assertEquals(UserStatus.OFFLINE, testUser.getStatus());
  }

  @Test
  public void getUsers_UsersFound_UsersReturned() {
      // when
      Mockito.when(userService.getUsers()).thenReturn(Collections.singletonList(testUser));

      // then
      assertEquals(testUser, userService.getUsers().stream().findFirst().get());
  }

  @Test
  public void getUser_IdFound_UserReturned() {
      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser);

      // then
      assertEquals(testUser, userService.findUserById(testUser.getUserId()));
  }

  // test if 404 (NOT_FOUND) is thrown when trying to get a non-existing user
  @Test
  public void getUser_IdNotFound_throwsException() {
      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(null);

      // then -> attempt to find the user by id -> check that an error
      // is thrown
      assertThrows(ResponseStatusException.class, () -> userService.findUserById(Mockito.anyInt()));
  }

  // test if 404 (NOT_FOUND) is thrown when trying to update a non-existing user
  @Test
  public void updateUser_IdNotFound_throwsException() {
      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(null);

      // then -> attempt to find the user by id -> check that an error
      // is thrown
      assertThrows(ResponseStatusException.class, () -> userService.updateUser(testUser, Mockito.anyInt()));
  }

  @Test
  public void updateUser_validInputs_success() {
      // given
      User user = userService.createUser(testUser);
      user.setEmail("abc@uzh.ch");
      user.setUsername("anotherUsername");
      user.setPassword("1234");

      // when -> setup additional mocks for UserRepository
      Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser);
      userService.updateUser(user, testUser.getUserId());

      // then
      assertEquals(user, testUser);
  }

}
