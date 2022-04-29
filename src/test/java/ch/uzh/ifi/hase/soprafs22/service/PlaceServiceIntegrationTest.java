package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.PlaceRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
@SpringBootTest
public class PlaceServiceIntegrationTest {

  @Qualifier("placeRepository")
  @Autowired
  private PlaceRepository placeRepository;

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlaceService placeService;

  @BeforeEach
  public void setup() {
    placeRepository.deleteAll();
  }

  @Test
  public void createPlace_validInputs_success() {
    // given
    assertNull(placeRepository.findByProviderId(1));

   Place testPlace = new Place();
   testPlace.setProviderId(1);
   testPlace.setName("testName");
   testPlace.setAddress("Universitätsstrasse 1");
   testPlace.setClosestCampus(Campus.CENTER);
   testPlace.setDescription("this is my room.");
   testPlace.setPictureOfThePlace("some link");

    // when
    Place createdPlace = placeService.createPlace(testPlace);

    // then
    assertEquals(testPlace.getPlaceId(), createdPlace.getPlaceId());
    assertEquals(testPlace.getProviderId(), createdPlace.getProviderId());
    assertEquals(testPlace.getName(), createdPlace.getName());
    assertEquals(testPlace.getAddress(), createdPlace.getAddress());
    assertEquals(Campus.CENTER, createdPlace.getClosestCampus());
    assertEquals(testPlace.getDescription(), createdPlace.getDescription());
    assertEquals(testPlace.getPictureOfThePlace(), createdPlace.getPictureOfThePlace());
  }

  @Test
  public void createPlace_duplicateUser_throwsException() {
    assertNull(placeRepository.findByPlaceId(1));

    Place testPlace = new Place();
    testPlace.setName("testName");
    testPlace.setAddress("Universitätsstrasse 1");
    testPlace.setClosestCampus(Campus.CENTER);
    testPlace.setDescription("this is my room.");
    testPlace.setPictureOfThePlace("some link");
    Place createdPlace = placeService.createPlace(testPlace);

    // attempt to create second place with same user
    Place testPlace2 = new Place();

    testPlace2.setPlaceId(1);

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> placeService.createPlace(testPlace2));
  }

  @Test
  public void getAllPlacesForUser_UserNotFound_throwsException() {
      User user = new User();
      User user2 = new User();

      Place testPlace = new Place();
      testPlace.setProviderId(user.getUserId());

      placeService.getPlaces().add(testPlace);

      assertThrows(ResponseStatusException.class, () -> placeService.getAllPlacesForUser(user2.getUserId()));
  }

  @Test
  public void deletePlace_validInputs_success() {
      Place testPlace = new Place();
      testPlace.setName("testName");
      testPlace.setAddress("Universitätsstrasse 1");
      testPlace.setClosestCampus(Campus.CENTER);
      testPlace.setDescription("this is my room.");
      testPlace.setPictureOfThePlace("some link");
      placeService.createPlace(testPlace);

      placeService.deletePlace(testPlace.getPlaceId());

      assertTrue(placeService.getPlaces().isEmpty());
  }

    @Test
  public void updatePlace_IdNotFound_throwsException() {
      assertNull(placeRepository.findByPlaceId(1));

      // create new place, but it will not get saved it into the database
      Place placeUpdates = new Place();
      placeUpdates.setName("testName");
      placeUpdates.setAddress("Universitätsstrasse 1");
      placeUpdates.setClosestCampus(Campus.CENTER);
      placeUpdates.setDescription("this is my room.");
      placeUpdates.setPictureOfThePlace("some link");

      // check that an error is thrown
      assertThrows(ResponseStatusException.class, () -> placeService.updatePlace(placeUpdates, 1));
  }

  @Test
  public void updatePlace_validInputs_success() {
      Place testPlace = new Place();
      testPlace.setName("testName");
      testPlace.setAddress("Universitätsstrasse 1");
      testPlace.setClosestCampus(Campus.CENTER);
      testPlace.setDescription("this is my room.");
      testPlace.setPictureOfThePlace("some link");
      Place createdPlace = placeService.createPlace(testPlace);

      createdPlace.setName("another testname");
      createdPlace.setAddress("Oerlikonerstrasse 1");
      createdPlace.setClosestCampus(Campus.OERLIKON);
      createdPlace.setDescription("this is my other room.");
      createdPlace.setPictureOfThePlace("some other link");

      placeService.updatePlace(createdPlace, testPlace.getPlaceId());

      assertEquals(createdPlace, testPlace);
  }


}
