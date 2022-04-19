package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlaceGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.PlacePostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapperPlace {

  DTOMapperPlace INSTANCE = Mappers.getMapper(DTOMapperPlace.class);

  @Mapping(source = "providerId", target = "providerId")
  //@Mapping(source = "location", target = "location")
  @Mapping(source = "description", target = "description")
  Place convertPlacePostDTOtoEntity(PlacePostDTO placePostDTO);

  @Mapping(source = "placeId", target = "placeId")
  @Mapping(source = "providerId", target = "providerId")
  @Mapping(source = "location", target = "location")
  @Mapping(source = "description", target = "description")
  @Mapping(source = "pictureOfThePlace", target = "pictureOfThePlace")
  //@Mapping(source = "sleepEvents", target = "sleepEvents")
  PlaceGetDTO convertEntityToPlaceGetDTO(Place place);
}
