package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.NotificationRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest {

  @Mock
  private NotificationRepository notificationRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private NotificationService notificationService;

  private Notification testNotification;
  private Notification anotherTestNotification;
  private List<Notification> myNotifications;
  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setUserId(1);

    testNotification = new Notification();
    testNotification.setNotificationId(5);
    testNotification.setMessage("some message");
    testNotification.setReceiverId(1);
    testNotification.setCreationDate(LocalDateTime.now());

    anotherTestNotification = new Notification();
    anotherTestNotification.setNotificationId(6);
    anotherTestNotification.setMessage("some other message");
    anotherTestNotification.setReceiverId(1);
    anotherTestNotification.setCreationDate(LocalDateTime.now());

    myNotifications = new ArrayList<>();
    myNotifications.add(testNotification);
    myNotifications.add(anotherTestNotification);

    testUser.setMyNotifications(myNotifications);

    // when
    Mockito.when(notificationRepository.save(Mockito.any())).thenReturn(testNotification);
    Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser);
  }


  @Test
  public void createNotification_validInputs_success() {
    // when
    Notification createdNotification = notificationService.createNotification(testNotification.getReceiverId(), testNotification);

    // then
    Mockito.verify(notificationRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(createdNotification, testUser.getMyNotifications().get(0));
    assertEquals(testNotification.getNotificationId(), createdNotification.getNotificationId());
    assertEquals(testNotification.getMessage(), createdNotification.getMessage());
    assertEquals(testNotification.getReceiverId(), createdNotification.getReceiverId());
    assertEquals(testNotification.getCreationDate(), createdNotification.getCreationDate());
  }


  @Test
  public void getAllNotificationsForUser_success() {
      // when
      List<Notification> allNotificationsForUser = notificationService.getAllNotificationsForUser(testUser.getUserId());

      // then
      assertEquals(testUser.getMyNotifications().get(0).getNotificationId(), allNotificationsForUser.get(0).getNotificationId());
      assertEquals(testUser.getMyNotifications().get(1).getNotificationId(), allNotificationsForUser.get(1).getNotificationId());
  }
}
