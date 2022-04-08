package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.OFFLINE);

    checkIfUserExists(newUser);

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public User login(String username, User userLoginCredentials){
      // find user by userId
      User userByUsername = userRepository.findByUsername(username);

      // user is not registered
      if(userByUsername == null){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                  "User not found. Are you registered yet?");
      }

      // check whether password is correct
      else if(!Objects.equals(userByUsername.getPassword(), userLoginCredentials.getPassword())){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "The password is not correct!");
      }

      // check whether username is correct
      else if(!Objects.equals(userByUsername.getUsername(), userLoginCredentials.getUsername())){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "The username is not correct!");
      }

      // update status
      userByUsername.setStatus(UserStatus.ONLINE);

      return userByUsername;
  }

  public User logout(int userId){
      // find user by userId
      User userById = userRepository.findByUserId(userId);

      // update status
      userById.setStatus(UserStatus.OFFLINE);

      return userById;
  }

  public User updateUser(User userUpdated, int id) {
      /*// check that if user wants to update username to NULL (meaning he leaves the field empty)
      if (userUpdated.getUsername() == null) {
          throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                  "Username cannot be NULL! If you do not want to change your username, just enter your current one");
      }*/
      // find user by ID
      User UpdateUser = userRepository.findByUserId(id);

      // throw exception if user doesn't exist
      if (UpdateUser == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                  "The user was not found!");
      }

      /*String UpdatedUsername = userUpdated.getUsername();
      LocalDate UpdatedDateOfBirth = userUpdated.getDateOfBirth();

      // check if username already exists or not -> to make sure that the username will stay unique
      if(userRepository.findByUsername(userUpdated.getUsername()) != null){
          // username already exists
          // -> check if it's the user itself or another user
          checkIfTokenIsEqual(userUpdated, id);
      }
      // keep username and date of birth up to date
      UpdateUser.setUsername(UpdatedUsername);
      UpdateUser.setDateOfBirth(UpdatedDateOfBirth);*/

      return UpdateUser;
  }


  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    User userByEmail = userRepository.findByEmail(userToBeCreated.getEmail());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null && userByEmail != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username and the email", "are"));
    } else if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "username", "is"));
    } else if (userByEmail != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(baseErrorMessage, "email", "is"));
    }
  }

  // method is used to get the user info for the user profile
  // returns a single user by ID
  public User findUserById(int id) {
      User userById = userRepository.findByUserId(id);

      String baseErrorMessage = "The %s provided %s not found!";
      if (userById == null) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                  String.format(baseErrorMessage, "Id", "is"));
        }
      return userById;
    }

}
