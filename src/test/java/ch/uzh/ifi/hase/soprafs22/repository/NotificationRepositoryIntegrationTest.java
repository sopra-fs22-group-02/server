package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class NotificationRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void findByPlaceId_success() {
        // given
        Notification notification = new Notification();
        notification.setMessage("some message");
        notification.setReceiverId(2);
        notification.setCreationDate(LocalDateTime.now());

        entityManager.persist(notification);
        entityManager.flush();

        // when
        Notification found = notificationRepository.findByNotificationId(notification.getNotificationId());

        // then
        assertEquals(found.getNotificationId(), notification.getNotificationId());
        assertEquals(found.getMessage(), notification.getMessage());
        assertEquals(found.getReceiverId(), notification.getReceiverId());
        assertEquals(found.getCreationDate(), notification.getCreationDate());
    }
}
