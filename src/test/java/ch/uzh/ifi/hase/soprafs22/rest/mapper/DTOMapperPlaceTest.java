package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlaceGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlacePostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperPlaceTest {
  @Test
  public void testCreatePlace_fromPlacePostDTO_toPlace_success() {
    // create PlacePostDTO
    PlacePostDTO placePostDTO = new PlacePostDTO();
    placePostDTO.setName("name");
    placePostDTO.setAddress("Universitätsstrasse 1");
    placePostDTO.setClosestCampus(Campus.CENTER);
    placePostDTO.setDescription("this is my room");

    // MAP -> Create place
    Place place = DTOMapperPlace.INSTANCE.convertPlacePostDTOtoEntity(placePostDTO);

    // check content
    assertEquals(placePostDTO.getName(), place.getName());
    assertEquals(placePostDTO.getAddress(), place.getAddress());
    assertEquals(placePostDTO.getClosestCampus(), place.getClosestCampus());
    assertEquals(placePostDTO.getDescription(), place.getDescription());
  }

  @Test
  public void testGetPlace_fromPlace_toPlaceGetDTO_success() {
    // create Place
    Place place = new Place();
    place.setProviderId(3);
    place.setName("name");
    place.setAddress("Universitätsstrasse 1");
    place.setClosestCampus(Campus.CENTER);
    place.setDescription("this is my room");
    place.setPictureOfThePlace("some link");

    // MAP -> Create PlaceGetDTO
    PlaceGetDTO placeGetDTO = DTOMapperPlace.INSTANCE.convertEntityToPlaceGetDTO(place);

    // check content
    assertEquals(place.getPlaceId(), placeGetDTO.getPlaceId());
    assertEquals(place.getProviderId(), placeGetDTO.getProviderId());
    assertEquals(place.getName(), placeGetDTO.getName());
    assertEquals(place.getAddress(), placeGetDTO.getAddress());
    assertEquals(place.getClosestCampus(), placeGetDTO.getClosestCampus());
    assertEquals(place.getDescription(), placeGetDTO.getDescription());
    assertEquals(place.getPictureOfThePlace(), placeGetDTO.getPictureOfThePlace());
  }
}
