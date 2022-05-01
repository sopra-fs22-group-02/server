package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import ch.uzh.ifi.hase.soprafs22.rest.dto.NotificationPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.NotificationGetDTO;
import ch.uzh.ifi.hase.soprafs22.service.NotificationService;
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

import java.time.LocalDateTime;
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
@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NotificationService notificationService;

  @Test
  public void GetAllNotificationsForUser_thenReturnJsonArray() throws Exception {
    // given
    Notification notification = new Notification();
    notification.setNotificationId(5);
    notification.setMessage("some message");
    notification.setReceiverId(1);
    notification.setCreationDate(LocalDateTime.now());

    List<Notification> allNotificationsForUser= Collections.singletonList(notification);

    given(notificationService.getAllNotificationsForUser(Mockito.anyInt())).willReturn(allNotificationsForUser);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users/1/notifications")
            .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].message", is(notification.getMessage())));
  }

  @Test
  public void createNotification_validInput_userCreated() throws Exception {
    // given
    Notification notification = new Notification();
    notification.setNotificationId(5);
    notification.setMessage("some message");
    notification.setReceiverId(1);
    notification.setCreationDate(LocalDateTime.now());

    NotificationPostDTO notificationPostDTO = new NotificationPostDTO();
    notificationPostDTO.setMessage("some message");

    given(notificationService.createNotification(Mockito.anyInt(), Mockito.any())).willReturn(notification);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users/1/notifications")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(notificationPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.message", is(notification.getMessage())));
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