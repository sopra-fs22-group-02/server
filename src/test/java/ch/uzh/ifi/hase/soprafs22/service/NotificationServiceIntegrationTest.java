package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Campus;
import ch.uzh.ifi.hase.soprafs22.constant.EventState;
import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import ch.uzh.ifi.hase.soprafs22.entity.Place;
import ch.uzh.ifi.hase.soprafs22.entity.SleepEvent;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.NotificationRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class NotificationServiceIntegrationTest {

  @Qualifier("notificationRepository")
  @Autowired
  private NotificationRepository notificationRepository;

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private NotificationService notificationService;

  @BeforeEach
  public void setup() {
      //fetch all users
      List<User> allUsers = userRepository.findAll();
      // clear every user's notifications
      for(User user : allUsers){
          user.setMyNotifications(Collections.emptyList());
      }

      userRepository.deleteAll();
      notificationRepository.deleteAll();
  }

  @Test
  public void createNotification_validInputs_success() {
      assertEquals(Collections.emptyList(), userRepository.findAll());
      assertEquals(Collections.emptyList(),(notificationRepository.findAll()));
      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");

      Notification testNotification = new Notification();
      testNotification.setMessage("some message");

      // when
      User createdUser = userService.createUser(testUser);
      Notification createdNotification = notificationService.createNotification(createdUser.getUserId(), testNotification);
      //User userByUserId = userRepository.findByUserId(createdUser.getUserId());
      //userByUserId.setMyNotifications(Collections.singletonList(createdNotification));

      // then
      assertEquals(testNotification.getNotificationId(), createdNotification.getNotificationId());
      assertEquals(testNotification.getMessage(), createdNotification.getMessage());
      assertEquals(testNotification.getReceiverId(), createdNotification.getReceiverId());
      assertEquals(testNotification.getCreationDate(), createdNotification.getCreationDate());
      // check whether the created notification is added to the receiver^s notifications
      //assertEquals(Collections.singletonList(createdNotification), userByUserId.getMyNotifications());
  }


  @Test
  public void getAllNotificationsForUser_success() {
      assertEquals(Collections.emptyList(), userRepository.findAll());
      assertEquals(Collections.emptyList(),(notificationRepository.findAll()));

      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");

      Notification testNotification = new Notification();
      testNotification.setMessage("some message");

      User createdUser = userService.createUser(testUser);
      Notification createdNotification = notificationService.createNotification(createdUser.getUserId(), testNotification);
      User userByUserId = userRepository.findByUserId(createdUser.getUserId());
      //userByUserId.setMyNotifications(Collections.singletonList(createdNotification));
      //System.out.println("notifications: " + userByUserId.getMyNotifications());

      Notification anothertestNotification = new Notification();
      anothertestNotification.setMessage("some other message");

      Notification anotherCreatedNotification = notificationService.createNotification(createdUser.getUserId(), anothertestNotification);
      userByUserId.setMyNotifications(Arrays.asList(createdNotification, anotherCreatedNotification));

      System.out.println(userByUserId.getMyNotifications());

      // when
      Notification foundNotification = Collections.singletonList(testNotification).get(0);
      Notification anotherFoundNotification = notificationService.getAllNotificationsForUser(createdUser.getUserId()).get(0);
      Notification foundNotification2 = Collections.singletonList(anothertestNotification).get(0);
      Notification anotherFoundNotifcation2 = notificationService.getAllNotificationsForUser(createdUser.getUserId()).get(1);

      // then
      assertEquals(foundNotification.getNotificationId(), anotherFoundNotification.getNotificationId());
      assertEquals(foundNotification2.getNotificationId(), anotherFoundNotifcation2.getNotificationId());
  }

  @Test
  public void checkIfOlderThan24h_deleteNotification_success() {
      assertEquals(Collections.emptyList(), userRepository.findAll());
      assertEquals(Collections.emptyList(),(notificationRepository.findAll()));

      // given
      User testUser = new User();
      testUser.setEmail("firstname.lastname@uzh.ch");
      testUser.setUsername("testUsername");
      testUser.setPassword("password");
      User createdUser = userService.createUser(testUser);

      Notification testNotification = new Notification();
      testNotification.setMessage("some message");
      testNotification.setReceiverId(createdUser.getUserId());
      testNotification.setCreationDate(LocalDateTime.now().minusHours(25));
      testNotification = notificationRepository.save(testNotification);
      notificationRepository.flush();

      // when
      notificationService.checkIfOlderThan24h();

      // then
      assertNull(notificationRepository.findByNotificationId(testNotification.getNotificationId()));
  }

}
