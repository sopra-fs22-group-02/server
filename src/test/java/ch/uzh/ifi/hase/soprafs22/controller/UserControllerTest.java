package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void givenUsers_whenGetAllUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    user.setEmail("xyz@uzh.ch");
    user.setUsername("username");
    user.setPassword("1234");
    user.setStatus(UserStatus.OFFLINE);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].email", is(user.getEmail())))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].password", is(user.getPassword())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setUserId(1);
    user.setEmail("xyz@uzh.ch");
    user.setUsername("testUsername");
    user.setPassword("1234");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setEmail("Test User");
    userPostDTO.setUsername("testUsername");
    userPostDTO.setPassword("1234");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId", is(user.getUserId())))
        .andExpect(jsonPath("$.email", is(user.getEmail())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.password", is(user.getPassword())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())));
  }

  @Test
  public void UserLogin_validInputs_StatusChanged() throws Exception {
      // given
      User user = new User();
      user.setEmail("xyz@uzh.ch");
      user.setUsername("username");
      user.setPassword("1234");
      user.setStatus(UserStatus.ONLINE);
      User createdUser = userService.createUser(user);

      UserGetDTO userGetDTO = new UserGetDTO();
      userGetDTO.setEmail("xyz@uzh.ch");
      userGetDTO.setUsername("username");
      userGetDTO.setPassword("1234");

      given(userService.login(user.getUsername(), createdUser)).willReturn(user);

      // when
      MockHttpServletRequestBuilder postRequest = post("/users/username/login")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userGetDTO));

      // then
      mockMvc.perform(postRequest).andExpect(status().isOk());
  }

  @Test
  public void UserLogout_validInputs_StatusChanged() throws Exception {
      // given
      User user = new User();
      user.setEmail("xyz@uzh.ch");
      user.setUsername("username");
      user.setPassword("1234");
      user.setStatus(UserStatus.OFFLINE);
      User createdUser = userService.createUser(user);

      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setEmail("xyz@uzh.ch");
      userPostDTO.setUsername("username");
      userPostDTO.setPassword("1234");

      given(userService.login(user.getUsername(), createdUser)).willReturn(user);

      // when
      MockHttpServletRequestBuilder postRequest = post("/users/username/logout")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPostDTO));

      // then
      mockMvc.perform(postRequest).andExpect(status().isOk());
  }

  @Test
  public void getUser_validInput_singleUserReturned() throws Exception {
      //given
      User user = new User();
      user.setUserId(1);
      user.setEmail("xyz@uzh.ch");
      user.setUsername("testUsername");
      user.setPassword("1234");
      user.setToken("1");
      user.setStatus(UserStatus.ONLINE);
      user.setBio("hello");
      user.setProfilePicture("some link");

      UserGetDTO userGetDTO = new UserGetDTO();
      userGetDTO.setEmail("xyz@uzh.ch");
      userGetDTO.setFirstName("Peter");
      userGetDTO.setLastName("Muster");
      userGetDTO.setUsername("testUsername");
      userGetDTO.setPassword("1234");
      userGetDTO.setBio("hello");
      userGetDTO.setProfilePicture("some link");

      given(userService.findUserById(Mockito.anyInt())).willReturn(user);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder getRequest = get("/users/1/profile")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userGetDTO));

      // then
      mockMvc.perform(getRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.userId", is(user.getUserId())))
              .andExpect(jsonPath("$.email", is(user.getEmail())))
              .andExpect(jsonPath("$.username", is(user.getUsername())))
              .andExpect(jsonPath("$.password", is(user.getPassword())))
              .andExpect(jsonPath("$.token", is(user.getToken())))
              .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
              .andExpect(jsonPath("$.bio", is(user.getBio())))
              .andExpect(jsonPath("$.profilePicture", is(user.getProfilePicture())));

  }

  @Test
  public void updateUser_validInput_singleUserUpdated() throws Exception {
      // given
      User user = new User();
      user.setEmail("xyz@uzh.ch");
      user.setFirstName("Peter");
      user.setLastName("Muster");
      user.setUsername("testUsername");
      user.setPassword("1234");
      user.setToken("1");
      user.setStatus(UserStatus.ONLINE);

      User userUpdates = new User();
      userUpdates.setFirstName("Benno");
      userUpdates.setLastName("Hubmann");
      userUpdates.setUsername("anotherUsername");
      userUpdates.setPassword("4321");

      given(userService.updateUser(Mockito.any(),Mockito.anyInt())).willReturn(user);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/users/1/profile")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(user));

      // then
      mockMvc.perform(putRequest).andExpect(status().isOk())
              .andExpect(jsonPath("$.userId", is(user.getUserId())))
              .andExpect(jsonPath("$.email", is(user.getEmail())))
              .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
              .andExpect(jsonPath("$.lastName", is(user.getLastName())))
              .andExpect(jsonPath("$.username", is(user.getUsername())))
              .andExpect(jsonPath("$.password", is(user.getPassword())))
              .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
              .andExpect(jsonPath("$.token", is(user.getToken())));
  }


  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e));
    }
  }
}