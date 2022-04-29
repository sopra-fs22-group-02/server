package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.NotificationGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.NotificationPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperNotificationTest {
  @Test
  public void testCreateNotification_fromNotificationPostDTO_toNotification_success() {
    // create NotificationPostDTO
    NotificationPostDTO notificationPostDTO = new NotificationPostDTO();
    notificationPostDTO.setMessage("You have been accepted.");


    // MAP -> Create notification
    Notification notification = DTOMapperNotification.INSTANCE.convertNotificationPostDTOtoEntity(notificationPostDTO);

    // check content
    assertEquals(notificationPostDTO.getMessage(), notification.getMessage());
  }

  @Test
  public void testGetNotification_fromNotification_toNotificationGetDTO_success() {
    // create Notification
    Notification notification = new Notification();
    notification.setMessage("You have been accepted.");

    // MAP -> Create NotificationGetDTO
    NotificationGetDTO notificationGetDTO = DTOMapperNotification.INSTANCE.convertEntityToNotificationGetDTO(notification);

    // check content
    assertEquals(notification.getMessage(), notificationGetDTO.getMessage());
  }
}
