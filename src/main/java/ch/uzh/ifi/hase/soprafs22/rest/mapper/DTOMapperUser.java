package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import org.mapstruct.*;
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
public interface DTOMapperUser {

  DTOMapperUser INSTANCE = Mappers.getMapper(DTOMapperUser.class);

  @Mapping(source = "email", target = "email")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "firstName", target = "firstName")
  @Mapping(source = "lastName", target = "lastName")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "bio", target = "bio")
  @Mapping(source = "profilePicture", target = "profilePicture")
  @Mapping(source = "myNotifications", target = "myNotifications")
  UserGetDTO convertEntityToUserGetDTO(User user);
}
