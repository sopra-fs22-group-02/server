package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.ApplicationStatus;
import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.SleepEventPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.PlaceService;
import ch.uzh.ifi.hase.soprafs22.service.SleepEventService;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Long;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SleepEventControllerTest
 * This is a WebMvcTest which allows to test the SleepEventController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the SleepEventController works.
 */
@WebMvcTest(SleepEventController.class)
public class SleepEventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SleepEventService sleepEventService;
  @MockBean
  private UserService userService;
  @MockBean
  private PlaceService placeService;

  private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

  @Test
  public void givenSleepEvents_whenGetSleepEvents_thenReturnJsonArray() throws Exception {
    // given
      SleepEvent event = new SleepEvent();
      event.setProviderId(2);
      event.setPlaceId(3);
      event.setApplicants(null);
      event.setConfirmedApplicant(0);
      event.setStartDate(LocalDate.ofEpochDay(2023-1-1));
      event.setStartTime(LocalTime.parse("08:00:00"));
      event.setEndDate(LocalDate.ofEpochDay(2023-1-1));
      event.setEndTime(LocalTime.parse("20:00:00"));
      event.setState(EventState.AVAILABLE);
      event.setComment("some comment");

    List<SleepEvent> allSleepEventsOfThisPlace = Collections.singletonList(event);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(sleepEventService.getAllSleepEventsForPlace(Mockito.anyInt())).willReturn(allSleepEventsOfThisPlace);

    // when
    MockHttpServletRequestBuilder getRequest = get("/places/3/events")
            .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].eventId", is(event.getEventId())))
        .andExpect(jsonPath("$[0].providerId", is(event.getProviderId())))
        .andExpect(jsonPath("$[0].placeId", is(event.getPlaceId())))
        .andExpect(jsonPath("$[0].applicants", is(event.getApplicants())))
        .andExpect(jsonPath("$[0].confirmedApplicant", is(event.getConfirmedApplicant())))
        .andExpect(jsonPath("$[0].startDate", is(event.getStartDate().toString())))
        .andExpect(jsonPath("$[0].startTime", is(event.getStartTime().format(dtf))))
        .andExpect(jsonPath("$[0].endDate", is(event.getEndDate().toString())))
        .andExpect(jsonPath("$[0].endTime", is(event.getEndTime().format(dtf))))
        .andExpect(jsonPath("$[0].state", is(event.getState().toString())))
        .andExpect(jsonPath("$[0].comment", is(event.getComment())));
  }

  @Test
  public void createSleepEvent_validInput_sleepEventCreated() throws Exception {
    // given
    User user = new User();
    user.setUserId(1);
    user.setEmail("firstname.lastname@uzh.ch");
    user.setUsername("username");
    user.setFirstName("firstname");
    user.setLastName("lastname");
    user.setPassword("password");
    user.setStatus(UserStatus.ONLINE);
    user.setToken("1");
    user.setBio("Hi there, I'm a student at UZH!");
    user.setProfilePicture("some link");

    //User createdUser = userService.createUser(user);

    Place place = new Place();
    place.setPlaceId(2);
    place.setProviderId(user.getUserId());

    //Place createdPlace = placeService.createPlace(place);

    SleepEvent event = new SleepEvent();
    event.setEventId(3);
    event.setProviderId(user.getUserId());
    event.setPlaceId(place.getPlaceId());
    event.setApplicants(null);
    event.setConfirmedApplicant(0);
    event.setStartDate(null);
    event.setEndDate(null);
    event.setStartTime(null);
    event.setEndTime(null);
    event.setState(EventState.AVAILABLE);
    event.setComment("some comment");
    event.setApplicationStatus(null);
      /*System.out.println("place id in place: " + place.getPlaceId());
      System.out.println("place is in sleep event: " + event.getPlaceId());
      System.out.println("user id in user: " + user.getUserId());
      System.out.println("user id in event: " + event.getProviderId());*/

    SleepEventPostDTO sleepEventPostDTO = new SleepEventPostDTO();
    sleepEventPostDTO.setStartDate(null);
    sleepEventPostDTO.setEndDate(null);
    sleepEventPostDTO.setStartTime(null);
    sleepEventPostDTO.setEndTime(null);
    sleepEventPostDTO.setComment("some comment");

    given(sleepEventService.createSleepEvent(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).willReturn(event);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/places/1/2/events")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(sleepEventPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.eventId", is(event.getEventId())))
        .andExpect(jsonPath("$.providerId", is(event.getProviderId())))
        .andExpect(jsonPath("$.placeId", is(event.getPlaceId())))
        .andExpect(jsonPath("$.applicants", is(event.getApplicants())))
        .andExpect(jsonPath("$.confirmedApplicant", is(event.getConfirmedApplicant())))
        .andExpect(jsonPath("$.startDate", is(event.getStartDate())))
        .andExpect(jsonPath("$.endDate", is(event.getEndDate())))
        .andExpect(jsonPath("$.startTime", is(event.getStartTime())))
        .andExpect(jsonPath("$.endTime", is(event.getEndTime())))
        .andExpect(jsonPath("$.state", is(event.getState().toString())))
        .andExpect(jsonPath("$.comment", is(event.getComment())))
        .andExpect(jsonPath("$.applicationStatus", is(event.getApplicationStatus())));
  }
    @Test
    public void acceptAnApplicant_validInput() throws Exception {
        // given
        User user = new User();
        user.setUserId(2);
        user.setEmail("firstname.lastname@uzh.ch");
        user.setUsername("username");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setBio("Hi there, I'm a student at UZH!");
        user.setProfilePicture("some link");

        SleepEvent event = new SleepEvent();
        event.setEventId(4);
        event.setProviderId(1);
        event.setPlaceId(3);
        event.setApplicants(null);
        event.setConfirmedApplicant(2);
        event.setStartDate(LocalDate.ofEpochDay(2023-1-1));
        event.setStartTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setEndDate(LocalDate.ofEpochDay(2023-1-1));
        event.setEndTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setState(EventState.UNAVAILABLE);
        event.setApplicationStatus(ApplicationStatus.APPROVED);
        event.setComment("some comment");

        given(sleepEventService.confirmSleepEvent(Mockito.anyInt(), Mockito.anyInt())).willReturn(event);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/places/2/events/4/accept")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(event.getEventId())))
                .andExpect(jsonPath("$.providerId", is(event.getProviderId())))
                .andExpect(jsonPath("$.placeId", is(event.getPlaceId())))
                .andExpect(jsonPath("$.applicants", is(event.getApplicants())))
                .andExpect(jsonPath("$.confirmedApplicant", is(event.getConfirmedApplicant())))
                .andExpect(jsonPath("$.startDate", is(event.getStartDate().toString())))
                .andExpect(jsonPath("$.startTime", is(event.getStartTime().format(dtf))))
                .andExpect(jsonPath("$.endDate", is(event.getEndDate().toString())))
                .andExpect(jsonPath("$.endTime", is(event.getEndTime().format(dtf))))
                .andExpect(jsonPath("$.state", is(event.getState().toString())))
                .andExpect(jsonPath("$.comment", is(event.getComment())));
    }

    @Test
    public void addApplicant_validInput() throws Exception {
        // given
        User user = new User();
        user.setUserId(2);
        user.setEmail("firstname.lastname@uzh.ch");
        user.setUsername("username");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPassword("password");
        user.setStatus(UserStatus.ONLINE);
        user.setToken("1");
        user.setBio("Hi there, I'm a student at UZH!");
        user.setProfilePicture("some link");

        SleepEvent event = new SleepEvent();
        event.setEventId(4);
        event.setProviderId(1);
        event.setPlaceId(3);
        event.setApplicants(Collections.singletonList(2));
        event.setConfirmedApplicant(2);
        event.setStartDate(LocalDate.ofEpochDay(2023-1-1));
        event.setStartTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setEndDate(LocalDate.ofEpochDay(2023-1-1));
        event.setEndTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setState(EventState.AVAILABLE);
        event.setApplicationStatus(ApplicationStatus.PENDING);
        event.setComment("some comment");

        given(sleepEventService.addApplicant(Mockito.anyInt(), Mockito.anyInt())).willReturn(event);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/places/2/events/4")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(event.getEventId())))
                .andExpect(jsonPath("$.providerId", is(event.getProviderId())))
                .andExpect(jsonPath("$.placeId", is(event.getPlaceId())))
                .andExpect(jsonPath("$.applicants", is(event.getApplicants())))
                .andExpect(jsonPath("$.confirmedApplicant", is(event.getConfirmedApplicant())))
                .andExpect(jsonPath("$.startDate", is(event.getStartDate().toString())))
                .andExpect(jsonPath("$.startTime", is(event.getStartTime().format(dtf))))
                .andExpect(jsonPath("$.endDate", is(event.getEndDate().toString())))
                .andExpect(jsonPath("$.endTime", is(event.getEndTime().format(dtf))))
                .andExpect(jsonPath("$.state", is(event.getState().toString())))
                .andExpect(jsonPath("$.comment", is(event.getComment())));
    }

    @Test
    public void getAllEventsOfOnePlace_validInput() throws Exception {
        // given
        SleepEvent event = new SleepEvent();
        event.setEventId(3);
        event.setProviderId(1);
        event.setPlaceId(2);
        event.setApplicants(null);
        event.setConfirmedApplicant(0);
        event.setStartDate(LocalDate.ofEpochDay(2023-1-1));
        event.setStartTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setEndDate(LocalDate.ofEpochDay(2023-1-1));
        event.setEndTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setState(EventState.AVAILABLE);
        event.setComment("some comment");

        List<SleepEvent> allSleepEventOfThisPlace = Collections.singletonList(event);

        given(sleepEventService.getAllSleepEventsForPlace(Mockito.anyInt())).willReturn(allSleepEventOfThisPlace);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/places/2/events")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId", is(event.getEventId())))
                .andExpect(jsonPath("$[0].providerId", is(event.getProviderId())))
                .andExpect(jsonPath("$[0].placeId", is(event.getPlaceId())))
                .andExpect(jsonPath("$[0].applicants", is(event.getApplicants())))
                .andExpect(jsonPath("$[0].confirmedApplicant", is(event.getConfirmedApplicant())))
                .andExpect(jsonPath("$[0].startDate", is(event.getStartDate().toString())))
                .andExpect(jsonPath("$[0].startTime", is(event.getStartTime().format(dtf))))
                .andExpect(jsonPath("$[0].endDate", is(event.getEndDate().toString())))
                .andExpect(jsonPath("$[0].endTime", is(event.getEndTime().format(dtf))))
                .andExpect(jsonPath("$[0].state", is(event.getState().toString())))
                .andExpect(jsonPath("$[0].comment", is(event.getComment())));
    }

    @Test
    public void getAllAvailableEventsOfOnePlace_validInput() throws Exception {
        // given
        SleepEvent event = new SleepEvent();
        event.setEventId(3);
        event.setProviderId(1);
        event.setPlaceId(2);
        event.setApplicants(null);
        event.setConfirmedApplicant(0);
        event.setStartDate(LocalDate.ofEpochDay(2023-1-1));
        event.setStartTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setEndDate(LocalDate.ofEpochDay(2023-1-1));
        event.setEndTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setState(EventState.AVAILABLE);
        event.setComment("some comment");

        List<SleepEvent> allSleepEventOfThisPlace = new ArrayList<>();
        allSleepEventOfThisPlace.add(event);

        given(sleepEventService.getAllAvailableSleepEventsForPlace(Mockito.anyInt())).willReturn(allSleepEventOfThisPlace);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/places/2/events/available")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId", is(event.getEventId())))
                .andExpect(jsonPath("$[0].providerId", is(event.getProviderId())))
                .andExpect(jsonPath("$[0].placeId", is(event.getPlaceId())))
                .andExpect(jsonPath("$[0].applicants", is(event.getApplicants())))
                .andExpect(jsonPath("$[0].confirmedApplicant", is(event.getConfirmedApplicant())))
                .andExpect(jsonPath("$[0].startDate", is(event.getStartDate().toString())))
                .andExpect(jsonPath("$[0].startTime", is(event.getStartTime().format(dtf))))
                .andExpect(jsonPath("$[0].endDate", is(event.getEndDate().toString())))
                .andExpect(jsonPath("$[0].endTime", is(event.getEndTime().format(dtf))))
                .andExpect(jsonPath("$[0].state", is(event.getState().toString())))
                .andExpect(jsonPath("$[0].comment", is(event.getComment())));
    }

    @Test
    public void getSingleEvent_validInput() throws Exception {
        // given
        SleepEvent event = new SleepEvent();
        event.setProviderId(2);
        event.setPlaceId(3);
        event.setApplicants(null);
        event.setConfirmedApplicant(0);
        event.setStartDate(LocalDate.ofEpochDay(2023-1-1));
        event.setStartTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setEndDate(LocalDate.ofEpochDay(2023-1-1));
        event.setEndTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setState(EventState.AVAILABLE);
        event.setComment("some comment");

        given(sleepEventService.findSleepEventById(Mockito.anyInt())).willReturn(event);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder getRequest = get("/places/events/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(event.getEventId())))
                .andExpect(jsonPath("$.providerId", is(event.getProviderId())))
                .andExpect(jsonPath("$.placeId", is(event.getPlaceId())))
                .andExpect(jsonPath("$.applicants", is(event.getApplicants())))
                .andExpect(jsonPath("$.confirmedApplicant", is(event.getConfirmedApplicant())))
                .andExpect(jsonPath("$.startDate", is(event.getStartDate().toString())))
                .andExpect(jsonPath("$.startTime", is(event.getStartTime().format(dtf))))
                .andExpect(jsonPath("$.endDate", is(event.getEndDate().toString())))
                .andExpect(jsonPath("$.endTime", is(event.getEndTime().format(dtf))))
                .andExpect(jsonPath("$.state", is(event.getState().toString())))
                .andExpect(jsonPath("$.comment", is(event.getComment())));
    }

    @Test
    public void updateEvent_validInput() throws Exception {
        // given
        SleepEvent event = new SleepEvent();
        event.setEventId(3);
        event.setProviderId(1);
        event.setPlaceId(2);
        event.setApplicants(null);
        event.setConfirmedApplicant(0);
        event.setStartDate(LocalDate.ofEpochDay(2023-1-1));
        event.setStartTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setEndDate(LocalDate.ofEpochDay(2023-1-1));
        event.setEndTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setState(EventState.AVAILABLE);
        event.setComment("some comment");

        SleepEvent eventUpdates = new SleepEvent();
        event.setStartDate(LocalDate.ofEpochDay(2023-1-2));
        event.setStartTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setEndDate(LocalDate.ofEpochDay(2023-1-2));
        event.setEndTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setState(EventState.AVAILABLE);
        event.setComment("some comment");

        given(sleepEventService.updateSleepEvent(Mockito.anyInt(), Mockito.anyInt(), Mockito.any())).willReturn(event);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder putRequest = put("/places/1/events/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(eventUpdates));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is(event.getEventId())))
                .andExpect(jsonPath("$.providerId", is(event.getProviderId())))
                .andExpect(jsonPath("$.placeId", is(event.getPlaceId())))
                .andExpect(jsonPath("$.applicants", is(event.getApplicants())))
                .andExpect(jsonPath("$.confirmedApplicant", is(event.getConfirmedApplicant())))
                .andExpect(jsonPath("$.startDate", is(event.getStartDate().toString())))
                .andExpect(jsonPath("$.startTime", is(event.getStartTime().format(dtf))))
                .andExpect(jsonPath("$.endDate", is(event.getEndDate().toString())))
                .andExpect(jsonPath("$.endTime", is(event.getEndTime().format(dtf))))
                .andExpect(jsonPath("$.state", is(event.getState().toString())))
                .andExpect(jsonPath("$.comment", is(event.getComment())));
    }

    @Test
    public void deleteEvent_validInput() throws Exception {
        // given
        SleepEvent event = new SleepEvent();
        event.setEventId(3);
        event.setProviderId(1);
        event.setPlaceId(2);
        event.setApplicants(null);
        event.setConfirmedApplicant(0);
        event.setStartDate(LocalDate.ofEpochDay(2023-1-1));
        event.setStartTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setEndDate(LocalDate.ofEpochDay(2023-1-1));
        event.setEndTime(LocalTime.parse((LocalTime.now().format(dtf))));
        event.setState(EventState.AVAILABLE);
        event.setComment("some comment");

        sleepEventService.deleteSleepEvent(event.getEventId(), event.getProviderId());

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder deleteRequest = delete("/places/1/events/3")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());
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