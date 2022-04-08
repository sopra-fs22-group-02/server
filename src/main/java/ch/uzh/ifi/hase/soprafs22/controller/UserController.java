package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */

@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

/** POST endpoints */

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // create user
        User createdUser = userService.createUser(userInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
    }


    @PostMapping("/users/{userId}/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO login(@PathVariable int userId, @RequestBody User userLoginCredentials){
        // check if user is registered
        User loggedInUser = userService.login(userId, userLoginCredentials);

        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(loggedInUser);
    }

    @PostMapping("/users/{userId}/logout")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void logout(@PathVariable int userId){

    }

/** GET endpoints */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserGetDTO> getAllUsers() {
        // fetch all users in the internal representation
        List<User> users = userService.getUsers();
        List<UserGetDTO> userGetDTOs = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
        }
        return userGetDTOs;
    }

    @GetMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void profile(@PathVariable int userId){

    }

    @GetMapping("/users/{userId}/notifications")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void notification(@PathVariable int userId){

    }

/** PUT endpoints */

    @PutMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updateProfile(@PathVariable int userId){

    }

    @PutMapping("/users/{userId}/notifications")
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public void updateNotification(@PathVariable int userId){

    }
}
