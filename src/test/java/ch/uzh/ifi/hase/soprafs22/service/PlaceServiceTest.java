package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.PlaceRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

public class PlaceServiceTest {

  @Mock
  private PlaceRepository placeRepository;

  @InjectMocks
  private PlaceService placeService;

  private Place testPlace;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testPlace = new Place();
    testPlace.setProviderId(1);
    testPlace.setName("testName");
    testPlace.setAddress("Universit√§tsstrasse 1");
    testPlace.setClosestCampus(Campus.CENTER);
    testPlace.setDescription("this is my room.");
    testPlace.setPictureOfThePlace("some link");

    // when -> any object is being save in the placeRepository -> return the dummy
    // testPlace
    Mockito.when(placeRepository.save(Mockito.any())).thenReturn(testPlace);
  }

  @Test
  public void createPlace_validInputs_success() {
    // when -> any object is being save in the placeRepository -> return the dummy
    // testPlace
    Place createdPlace = placeService.createPlace(testPlace);

    // then
    Mockito.verify(placeRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testPlace.getPlaceId(), createdPlace.getPlaceId());
    assertEquals(testPlace.getProviderId(), createdPlace.getProviderId());
    assertEquals(testPlace.getName(), createdPlace.getName());
    assertEquals(testPlace.getAddress(), createdPlace.getAddress());
    assertEquals(Campus.CENTER, createdPlace.getClosestCampus());
    assertEquals(testPlace.getDescription(), createdPlace.getDescription());
    assertEquals(testPlace.getPictureOfThePlace(), createdPlace.getPictureOfThePlace());
  }

  @Test
  public void createPlace_duplicatePlace_throwsException() {
    // given -> a first place has already been created
    placeService.createPlace(testPlace);

    // when -> setup additional mocks for PlaceRepository
    Mockito.when(placeRepository.findByProviderId(Mockito.anyInt())).thenReturn(testPlace);

    // then -> attempt to create second place with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> placeService.createPlace(testPlace));
  }

  @Test
  public void updatePlace_validInputs_success() {
      // given
      Place createdPlace = placeService.createPlace(testPlace);

      // when -> setup additional mocks for PlaceRepository
      Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);
      createdPlace.setAddress("Oerlikonerstrasse 1");
      placeService.updatePlace(createdPlace, Mockito.anyInt());

      assertEquals(testPlace.getAddress(), createdPlace.getAddress());
  }

  @Test
  public void updatePlace_IdNotFound_throwsException() {
      // when -> setup additional mocks for PlaceRepository
      Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(null);

      // then -> attempt to find the place by id -> check that an error
      // is thrown
      assertThrows(ResponseStatusException.class, () -> placeService.updatePlace(testPlace, Mockito.anyInt()));
  }

  @Test
  public void updatePlace_UpdateAddressToNull_throwsException() {
      // given
      placeService.createPlace(testPlace);

      // when -> setup additional mocks for PlaceRepository
      Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);
      testPlace.setAddress(null);
      // then -> attempt to update the place -> check that an error
      // is thrown
      assertThrows(ResponseStatusException.class, () -> placeService.updatePlace(testPlace, Mockito.anyInt()));
  }

  @Test
  public void updatePlace_UpdateNameToNull_throwsException() {
      // given
      placeService.createPlace(testPlace);

      // when -> setup additional mocks for PlaceRepository
      Mockito.when(placeRepository.findByPlaceId(Mockito.anyInt())).thenReturn(testPlace);
      testPlace.setName(null);
      // then -> attempt to update the place -> check that an error
      // is thrown
      assertThrows(ResponseStatusException.class, () -> placeService.updatePlace(testPlace, Mockito.anyInt()));
  }

    @Test
    public void deletePlace_validInputs_success() {
        // given
        placeService.createPlace(testPlace);

        // when
        Mockito.when(placeRepository.findByProviderId(Mockito.anyInt())).thenReturn(testPlace);
        placeService.deletePlace(testPlace.getPlaceId());

        // then
        assertNull(placeRepository.findByPlaceId(testPlace.getPlaceId()));
    }

}
