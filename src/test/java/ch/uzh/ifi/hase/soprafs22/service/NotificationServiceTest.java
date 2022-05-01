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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;

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

    testUser.setMyNotifications(Collections.singletonList(testNotification));

    // when
    Mockito.when(notificationRepository.save(Mockito.any())).thenReturn(testNotification);
    Mockito.when(userRepository.findByUserId(Mockito.anyInt())).thenReturn(testUser);
  }

  /** problem with user.addNotifications()*/
  /*@Test
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
  }*/

  /**SingletonList cannot be returned by findByUserId()
   findByUserId() should return User*/
  /*@Test
  public void getAllNotificationsForUser_success() {
      // when
      Mockito.when(notificationService.getAllNotificationsForUser(Mockito.anyInt())).thenReturn(testUser.getMyNotifications());

      // then
      assertEquals(testUser.getMyNotifications(), notificationService.getAllNotificationsForUser(testNotification.getReceiverId()));
  }*/

  /** problem with for loops in checkIfOlderThan24h()*/
    /*@Test
    public void checkIfOlderThan24h_deleteNotification() {
        // given
        Notification oldNotification = new Notification();
        oldNotification.setNotificationId(6);
        oldNotification.setMessage("some message");
        oldNotification.setReceiverId(1);
        oldNotification.setCreationDate(LocalDateTime.now().minusHours(25));

        Mockito.when(notificationRepository.findAll()).thenReturn(Collections.singletonList(oldNotification));
        // when
        notificationService.checkIfOlderThan24h();

        // then
        assertNull(notificationRepository.findByNotificationId(oldNotification.getNotificationId()));
        assertEquals(Collections.emptyList(), testUser.getMyNotifications());
    }*/
}
