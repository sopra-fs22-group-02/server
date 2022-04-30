package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlacePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.service.PlaceService;
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

import static javax.swing.UIManager.put;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PlaceController.class)
public class PlaceControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PlaceService placeService;

  @Test
  public void givenPlaces_whenGetPlaces_thenReturnJsonArray() throws Exception {
    // given
    Place place = new Place();
    place.setName("name");
    place.setAddress("Universitätsstrasse 1");
    place.setClosestCampus(Campus.CENTER);
    place.setDescription("this is my room");

    List<Place> allPlaces = Collections.singletonList(place);

    // this mocks the PlaceService -> we define above what the placeService should
    // return when getPlaces() is called
    given(placeService.getPlaces()).willReturn(allPlaces);

    // when
    MockHttpServletRequestBuilder getRequest = get("/places").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].name", is(place.getName())))
        .andExpect(jsonPath("$[0].address", is(place.getAddress())))
        .andExpect(jsonPath("$[0].closestCampus", is(place.getClosestCampus().toString())))
        .andExpect(jsonPath("$[0].description", is(place.getDescription())));
  }

  @Test
  public void createPlace_validInput_placeCreated() throws Exception {
    // given
    Place place = new Place();
    place.setPlaceId(2);
    place.setProviderId(3);
    place.setName("testName");
    place.setAddress("Universitätsstrasse 1");
    place.setClosestCampus(Campus.CENTER);
    place.setDescription("this is my room");
    place.setPictureOfThePlace("some link");

    PlacePostDTO placePostDTO = new PlacePostDTO();
    placePostDTO.setName("name");
    placePostDTO.setAddress("Universitätsstrasse 1");
    placePostDTO.setClosestCampus(Campus.CENTER);
    placePostDTO.setDescription("this is my room");

    given(placeService.createPlace(Mockito.any())).willReturn(place);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/places")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(placePostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.placeId", is(place.getPlaceId())))
        .andExpect(jsonPath("$.providerId", is(place.getProviderId())))
        .andExpect(jsonPath("$.name", is(place.getName())))
        .andExpect(jsonPath("$.closestCampus", is(place.getClosestCampus().toString())))
        .andExpect(jsonPath("$.address", is(place.getAddress())))
        .andExpect(jsonPath("$.description", is(place.getDescription())));
  }

  @Test
  public void givenPlaces_whenGetAllPlacesForUser_thenReturnJsonArray() throws Exception {
      // given
      User user = new User();
      user.setUserId(1);
      Place place = new Place();
      place.setProviderId(user.getUserId());
      place.setName("name");
      place.setAddress("Universitätsstrasse 1");
      place.setClosestCampus(Campus.CENTER);
      place.setDescription("this is my room");

      List<Place> allPlaces = Collections.singletonList(place);

      // this mocks the PlaceService -> we define above what the placeService should
      // return when getPlaces() is called
      given(placeService.getAllPlacesForUser(user.getUserId())).willReturn(allPlaces);

      // when
      MockHttpServletRequestBuilder getRequest = get("/places/1").contentType(MediaType.APPLICATION_JSON);

      // then
      mockMvc.perform(getRequest).andExpect(status().isOk())
              .andExpect(jsonPath("$", hasSize(1)))
              .andExpect(jsonPath("$[0].placeId", is(place.getPlaceId())))
              .andExpect(jsonPath("$[0].providerId", is(place.getProviderId())))
              .andExpect(jsonPath("$[0].name", is(place.getName())))
              .andExpect(jsonPath("$[0].address", is(place.getAddress())))
              .andExpect(jsonPath("$[0].closestCampus", is(place.getClosestCampus().toString())))
              .andExpect(jsonPath("$[0].description", is(place.getDescription())));
  }

  @Test
  public void deletePlace_validInput_singlePlaceDeleted() throws Exception {
      // given
      Place place = new Place();
      place.setPlaceId(2);
      place.setName("name");
      place.setAddress("Universitätsstrasse 1");
      place.setClosestCampus(Campus.CENTER);
      place.setDescription("this is my room");

      placeService.deletePlace(place.getPlaceId());

      // when
      MockHttpServletRequestBuilder deleteRequest = delete("/places/2").contentType(MediaType.APPLICATION_JSON);

      // then
      mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
  }


  @Test
  public void updatePlace_validInput_singlePlaceUpdated() throws Exception {
      // given
      Place place = new Place();
      place.setName("name");
      place.setAddress("Universitätsstrasse 1");
      place.setClosestCampus(Campus.CENTER);
      place.setDescription("this is my room");

      Place placeUpdates = new Place();
      placeUpdates.setPlaceId(2);
      placeUpdates.setName("another name");
      placeUpdates.setAddress("Oerlikonerstrasse 1");
      placeUpdates.setClosestCampus(Campus.OERLIKON);
      placeUpdates.setDescription("this is my other room.");

      given(placeService.updatePlace(Mockito.any(),Mockito.anyInt())).willReturn(place);

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder putRequest = MockMvcRequestBuilders.put("/places/1")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(place));

      // then
      mockMvc.perform(putRequest).andExpect(status().isOk())
              .andExpect(jsonPath("$.placeId", is(place.getPlaceId())))
              .andExpect(jsonPath("$.providerId", is(place.getProviderId())))
              .andExpect(jsonPath("$.name", is(place.getName())))
              .andExpect(jsonPath("$.address", is(place.getAddress())))
              .andExpect(jsonPath("$.closestCampus", is(place.getClosestCampus().toString())))
              .andExpect(jsonPath("$.description", is(place.getDescription())));
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