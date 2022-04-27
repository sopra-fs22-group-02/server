package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import ch.uzh.ifi.hase.soprafs22.rest.dto.NotificationGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.NotificationPostDTO;
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
public interface DTOMapperNotification {

  DTOMapperNotification INSTANCE = Mappers.getMapper(DTOMapperNotification.class);

  @Mapping(source = "message", target = "message")
  @Mapping(source = "sender", target = "sender")
  @Mapping(source = "receiver", target = "receiver")
  Notification convertNotificationPostDTOtoEntity(NotificationPostDTO notificationPostDTO);

  @Mapping(source = "notificationId", target = "notificationId")
  @Mapping(source = "message", target = "message")
  @Mapping(source = "sender", target = "sender")
  @Mapping(source = "receiver", target = "receiver")
  NotificationGetDTO convertEntityToNotificationGetDTO(Notification notification);
}
